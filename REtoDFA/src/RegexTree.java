/**
 * @author leejiwon
 * 
 * Regular Expression Tree
 */
public class RegexTree{
	private Type type;
	private Character value;
	private RegexTree left;
	private RegexTree right;
	
	public RegexTree(Type type, Character value) {
		this.type = type;
		this.value = value;
		this.left = null;
		this.right = null;
	}
	
	public void setLeft(RegexTree left) {
		this.left = left;
	}
	
	public void setRight(RegexTree right) {
		this.right = right;
	}
	
	public Type getType() {
		return type;
	}
	
	public Character getValue() {
		return value;
	}
	
	public RegexTree getLeft() {
		return left;
	}
	
	public RegexTree getRight() {
		return right;
	}
}