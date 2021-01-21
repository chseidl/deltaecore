package solver.constraints;

import solver.constraints.Constraint;
import solver.constraints.LCF;

public class LCF2 {
	public static Constraint<?,?> implies(Constraint<?,?> constraint1, Constraint<?,?> constraint2) {
		//a -> b = !a || b
		return LCF.or(LCF.not(constraint1), constraint2);
	}
}
