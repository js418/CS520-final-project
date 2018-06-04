package final_project;

public class dataResult {
    protected bugInfo bugInfo;
    protected FLTechnique FLtech;
    protected double FLscore;

    public dataResult(bugInfo bugInfo, FLTechnique FLtech, Double FLscore){
        this.bugInfo = bugInfo;
        this.FLtech = FLtech;
        this.FLscore = FLscore;
    }

    public final_project.bugInfo getBugInfo() {
        return bugInfo;
    }

    public double getFLscore() {
        return FLscore;
    }

    public FLTechnique getFLtech() {
        return FLtech;
    }

    public void setBugInfo(bugInfo bugInfo) {
        this.bugInfo = bugInfo;
    }

    public void setFLLabel(FLTechnique FLLabel) {
        this.FLtech = FLLabel;
    }

    public void setFLscore(double FLscore) {
        this.FLscore = FLscore;
    }
}