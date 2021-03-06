package socialite.Absyn; // Java Package generated by the BNF Converter.

public class ColumnDecl extends ColumnDeclaration {
  public final TypeName typename_;
  public final Variable variable_;
  public final AggregateSpecifier aggregatespecifier_;

  public ColumnDecl(TypeName p1, Variable p2, AggregateSpecifier p3) { typename_ = p1; variable_ = p2; aggregatespecifier_ = p3; }

  public <R,A> R accept(socialite.Absyn.ColumnDeclaration.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof socialite.Absyn.ColumnDecl) {
      socialite.Absyn.ColumnDecl x = (socialite.Absyn.ColumnDecl)o;
      return this.typename_.equals(x.typename_) && this.variable_.equals(x.variable_) && this.aggregatespecifier_.equals(x.aggregatespecifier_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(37*(this.typename_.hashCode())+this.variable_.hashCode())+this.aggregatespecifier_.hashCode();
  }


}
