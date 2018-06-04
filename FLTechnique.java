package final_project;


public class FLTechnique {
    protected String family;
    protected String formula;
    protected String TotalDefn;
    protected String KillDefn;
    protected String HybridScheme;
    protected String AggregateDefn;

    public FLTechnique (String family,String formula,String TotalDefn, String KillDefn, String HybridScheme, String AggregateDefn){
        this.family = family;
        this.formula = formula;
        this.TotalDefn = TotalDefn;
        this.KillDefn = KillDefn;
        this.HybridScheme = HybridScheme;
        this.AggregateDefn = AggregateDefn;
    }

    public String getFamily() {
        return family;
    }

    public String getFormula() {
        return formula;
    }

    public String getTotalDefn() {
        return TotalDefn;
    }

    public String getKillDefn() {
        return KillDefn;
    }

    public String getHybridScheme() {
        return HybridScheme;
    }

    public String getAggregateDefn() {
        return AggregateDefn;
    }

    public String getLabel(){
        String FLtech = this.family + " " + this.formula + " " + this.TotalDefn + " " + this.KillDefn + " " + this.HybridScheme + " " + this.AggregateDefn;
        return FLtech;
    }
}