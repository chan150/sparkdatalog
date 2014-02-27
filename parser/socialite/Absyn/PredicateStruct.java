package socialite.Absyn; // Java Package generated by the BNF Converter.

public class PredicateStruct extends Predicate {
  public final Atom atom_;
  public final ListTerm listterm_;

  public PredicateStruct(Atom p1, ListTerm p2) { atom_ = p1; listterm_ = p2; }

  public <R,A> R accept(socialite.Absyn.Predicate.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof socialite.Absyn.PredicateStruct) {
      socialite.Absyn.PredicateStruct x = (socialite.Absyn.PredicateStruct)o;
      return this.atom_.equals(x.atom_) && this.listterm_.equals(x.listterm_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.atom_.hashCode())+this.listterm_.hashCode();
  }


}
