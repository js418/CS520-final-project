package final_project;

public class fullLabel {
	protected bugInfo bugInfo;
    protected FLTechnique FLtech;
    
    public fullLabel(bugInfo bugInfo, FLTechnique FLtech){
        this.bugInfo = bugInfo;
        this.FLtech = FLtech;
    }

    public final_project.bugInfo getBugInfo() {
        return bugInfo;
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

}
