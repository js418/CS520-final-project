package final_project;

public class bugInfo {
    protected String bugProject;
    protected String bugNum;

    public bugInfo(String bugProject,String bugNum){
        this.bugProject = bugProject;
        this.bugNum = bugNum;
    }

    public String getBugProject() {
        return bugProject;
    }

    public String getBugNum() {
        return bugNum;
    }

    public String getBugLabel(){
        String bug_label = this.bugProject + " " + this.bugNum;
        return  bug_label;
    }

}