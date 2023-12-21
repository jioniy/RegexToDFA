import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * Regular Expression : String to Tree
 * @author JiwonLee
 * 
 * 
 * convert regex to tree
 * 1. regex infix to postfix
 * 2. construct regex Tree
 * 
 */
public class RegexTreeConverter {
	
	
	// convert regex To Tree
	public static RegexTree convert(List<Character> alphabetSet, String regexp) {
		String pRegex = postfix(alphabetSet, regexp);//1. regex infix to postfix
		return constructTree(alphabetSet, pRegex);//2. construct regex Tree
	}
	
	
	// 1. postfix
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
	
    // 2. construct Tree
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
	
}