#!/usr/bin/env perl
#
# This script removes all defects from the data file for which the scores of some FL
# techniques are missing. TODO: This is not the most efficient solution
# The script also splits the data file into two files: 1) All scores for real faults and
# 2) all scores for artificial faults.
#
# usage: ./clean_up_scores.pl <data file> | gzip > <output file>

use strict;
use warnings;

# Total number of expected data points per defect:
# 4 debugging scenarios * (
#      {sbfl} 6 formulas * 2 total defs +
#      {mbfl} 6 formulas * 2 total defs + 6 kill defs + 4 hybrid schemes+ 2 aggregate defs) +
#    {hybrid} 5 hybrid techniques)
my $TOTAL= 4 * (6*2 + 6*2*6*4*2 + 5);

# Check arguments
$#ARGV>=0 or die "usage: $0 <score.csv file> (<number of expected data points per defect>)";
my $csv = $ARGV[0];
$TOTAL = $ARGV[1] // $TOTAL;

# Make sure the provided score.csv file exists
-e "$csv" or die "$csv file not found!";

my %all_bugs = ();

# Are we dealing with any artificial faults?
my $artificial = 0;

open(IN,   "<$csv");
open(REAL, ">scores_real.csv");
open(ART,  ">scores_artificial.csv");
while(<IN>) {
    # Skip header
    /Project/ and next;
    my ($project, $bug) = split /,/;
    # Initialize bugId counter if necessary
    unless (defined($all_bugs{"$project-$bug"})) {
        $all_bugs{"$project-$bug"} = 0;
    }
    # Increment bugId counter
    ++$all_bugs{"$project-$bug"};

    # Split the scores into scores for real and artificial faults
    if($bug<1000) {
        print(REAL $_);
    } else {
        print(ART $_);
        ++$artificial;
    }
}
close(REAL);
close(ART);

# Hash that holds all artificial and real faults that should be removed
my %black_list = ();

## Perform additional clean up and sanity checks for artificial faults
if ($artificial > 0) {
    foreach my $key (keys %all_bugs) {
        my ($pid, $bid) = split(/-/, $key);
        next if $bid > 1000;

        my $map = "$pid/$bid.mutants.map.csv";
        unless (-e "$map") {
            $black_list{"$pid-$bid"} = 1;
            warn "Mutant mapping ($map) not found!";
            next;
        }
        my $log = "$pid/$bid.mutants.log";
        unless (-e "$log") {
            $black_list{"$pid-$bid"} = 1;
            warn "Mutation log ($log) not found!";
        }
        my %cache = ();
        open(MAP, "<$map");
        while(<MAP>) {
            chomp;
            my ($art_bid, $mut_id) = split /,/;
            $cache{$mut_id} = $art_bid;
        }
        close(MAP);

        # A number of mutants inevitably cause outliers -- their inclusion would require a
        # proper candidates file for each of them.
        # TODO: exclude these mutants or add candidate files to avoid a  biases set of
        # artificial faults.
        my @exclude = ();
        open(LOG, "<$log");
        while(<LOG>) {
            my $line = $_;
            $line =~ /^(\d+):/ or die "Unexpected line in mutation log!";
            my $mut_id = $1;
            # Filter particular mutants ... e.g., filter all STD mutants
            $line =~ /^(\d+):STD:.*\|==> (<NO-OP>|null)/ and push(@exclude, $mut_id);
        }
        close(LOG);

        foreach my $mut_id (@exclude) {
            defined $cache{$mut_id} or next;
            $black_list{"$pid-$cache{$mut_id}"} = 1;
        }
    }
}

# Exclude all incomplete bugs (i.e., bugs for which some data points are missing)
seek(IN, 0, 0);
# Print csv file header
my $header = <IN>;
print($header);
while(<IN>) {
    my ($project, $bug) = split /,/;
    if ($all_bugs{"$project-$bug"} eq $TOTAL) {
        unless (defined($black_list{"$project-$bug"})) {
            print($_);
        }
    }
}
close(IN);
