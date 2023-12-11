import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * Regular Expression : String to Tree
 * @author leejiwon
 * 
 * 1. input validator
 * 
 * 2. convert regex to tree
 * 2-1. regex infix to postfix
 * 2-2. construct regex Tree
 * 
 */
public class RegexTreeConverter {

	//TODO Input Validator
	
	
	// convert regex To Tree
	public static RegexTree convert(List<Character> alphabetSet, String regexp) {
		String pRegex = postfix(alphabetSet, regexp);//RE 후위식 변환
		return constructTree(alphabetSet, pRegex);//RE TREE 구성
	}
	
	
	// postfix
    private static String postfix(List<Character> alphabetSet, String regexp) {// RE 후위식 표기로 전환
        List<Character> temp = new ArrayList<>();
        for (int i = 0; i < regexp.length(); i++) {
        	//곱 연산 처리 
            if (i != 0
                    && (alphabetSet.contains(regexp.charAt(i - 1)) || regexp.charAt(i - 1) == ')' || regexp.charAt(i - 1) == Type.KLEENE.getText())
                    && (alphabetSet.contains(regexp.charAt(i))  || regexp.charAt(i) == '(')) {
                temp.add(Type.CONCAT.getText());
            }
            temp.add(regexp.charAt(i));
        }
        
        regexp = temp.toString().replaceAll("[\\[\\], ]", "");
        
        Stack<Character> stack = new Stack<>();
        String output = "";
        for (char c : regexp.toCharArray()) {
            if (alphabetSet.contains(c)) {//알파벳 처리
                output += c;
                continue;
            }
            if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output += stack.pop();
                }
                stack.pop();
            } else if (c == '(') {
                stack.push(c);
            } else if (c == Type.KLEENE.getText()) {
                output += c;
            } else if (stack.isEmpty() || stack.peek() == '(' || Type.isHigherPriority(c, stack.peek())) {
                stack.push(c);
            } else {
                while (!stack.isEmpty() && stack.peek() != '(' && !Type.isHigherPriority(c, stack.peek())) {
                    output += stack.pop();
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            output += stack.pop();
        }
        return output;
    }
	
    // construct Tree
	private static RegexTree constructTree(List<Character> alphabetSet, String pRegexp) {//RegexTree 구성
        Stack<RegexTree> stack = new Stack<>();
        for (char c : pRegexp.toCharArray()) {
            if (alphabetSet.contains(c)) {
                stack.push(new RegexTree(Type.SYMBOL, c));
            } else {
            	RegexTree z;
                if (c == Type.KLEENE.getText()) {
                    z = new RegexTree(Type.KLEENE, null);
                    z.setLeft(stack.pop());
                } else if (c == Type.CONCAT.getText()) {
                    z = new RegexTree(Type.CONCAT, null);
                    z.setRight(stack.pop());
                    z.setLeft(stack.pop());
                } else if (c == Type.UNION.getText()) {
                    z = new RegexTree(Type.UNION, null);
                    z.setRight(stack.pop());
                    z.setLeft(stack.pop());
                } else {
                    continue;
                }
                stack.push(z);
            }
        }
        return stack.pop();
    }
	
	
	private static void inorder(RegexTree rt) { // RegexTree 중위순회 - check 용
	    if (rt.getType() == Type.SYMBOL) {
	        System.out.print(rt.getValue());
	    } else if (rt.getType() == Type.CONCAT) {
	        inorder(rt.getLeft());
	        System.out.print(".");
	        inorder(rt.getRight());
	    } else if (rt.getType() == Type.UNION) {
	        inorder(rt.getLeft());
	        System.out.print("+");
	        inorder(rt.getRight());
	    } else if (rt.getType() == Type.KLEENE) {
	        inorder(rt.getLeft());
	        System.out.print("*");
	    }
	}

}
