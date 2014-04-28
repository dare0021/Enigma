package enigma;

public class MultiplierLogic {
	private int lhsatk, lhsdef, rhsatk, rhsdef;
	private double lhsatkmod, lhsdefmod, rhsatkmod, rhsdefmod;
	public MultiplierLogic(){init(0,0,0,0,0,0,0,0);}
	public MultiplierLogic(int la, int ld, int ra, int rd){init(la, ld, ra, rd, 0,0,0,0);}
	public MultiplierLogic(MultiplierLogic ml){
		init(	ml.getRawAtk(true),
				ml.getRawDef(true),
				ml.getRawAtk(false),
				ml.getRawDef(false),
				ml.getAtkmod(true),
				ml.getDefmod(true),
				ml.getAtkmod(false),
				ml.getDefmod(false));
	}private void init(int la, int ld, int ra, int rd, double lam, double ldm, double ram, double rdm){
		lhsatk = la;
		lhsdef = ld;
		rhsatk = ra;
		rhsdef = rd;
		lhsatkmod = lam;
		lhsdefmod = ldm;
		rhsatkmod = ram;
		rhsdefmod = rdm;
	}
	
	public int getRawAtk(boolean isLHS){
		if(isLHS)
			return lhsatk;
		else
			return rhsatk;
	}public int getRawDef(boolean isLHS){
		if(isLHS)
			return lhsdef;
		else
			return rhsdef;
	}
	
	public int getEffAtk(boolean isLHS){
		if(isLHS)
			return (int)((double)lhsatk * lhsatkmod);
		else
			return (int)((double)rhsatk * rhsatkmod);
	}public int getEffDef(boolean isLHS){
		if(isLHS)
			return (int)((double)lhsdef * lhsdefmod);
		else
			return (int)((double)rhsdef * rhsdefmod);
	}
	
	public void setAtk(boolean isLHS, int atk){
		if(isLHS)
			lhsatk = atk;
		else
			rhsatk = atk;
	}public void setDef(boolean isLHS, int def){
		if(isLHS)
			lhsdef = def;
		else
			rhsdef = def;
	}
	
	public double getAtkmod(boolean isLHS){
		if(isLHS)
			return lhsatkmod;
		else
			return rhsatkmod;
	}public double getDefmod(boolean isLHS){
		if(isLHS)
			return lhsdefmod;
		else
			return rhsdefmod;
	}
	
	public void setAtkmod(boolean isLHS, double mod){
		if(isLHS)
			lhsatkmod = mod;
		else
			rhsatkmod = mod;
	}public void setDefmod(boolean isLHS, double mod){
		if(isLHS)
			lhsdefmod = mod;
		else
			rhsdefmod = mod;
	}
	
	public void addAtkmod(boolean isLHS, double mod){
		if(isLHS)
			lhsatkmod += mod;
		else
			lhsatkmod += mod;
	}public void addDefmod(boolean isLHS, double mod){
		if(isLHS)
			lhsdefmod += mod;
		else
			rhsdefmod += mod;
	}
	
	public void mulAtkmod(boolean isLHS, double mod){
		if(isLHS)
			lhsatkmod *= mod;
		else
			rhsatkmod *= mod;
	}public void mulDefmod(boolean isLHS, double mod){
		if(isLHS)
			lhsdefmod *= mod;
		else
			rhsdefmod *= mod;
	}
}
