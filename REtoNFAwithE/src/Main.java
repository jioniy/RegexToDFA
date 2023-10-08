import java.util.Stack;
import java.util.ArrayList;
import java.util.Scanner;

enum Type{
	SYMBOL(' ', 0),
	KLEENE('*', 1),
	CONCAT('.', 2),
	UNION('+', 3);
	
	private final Character text;
	private final int index;
	
	Type(Character text, int index){
		this.text = text;
		this.index = index;
	}
	
	public Character getText() {
		return text;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static boolean higherPrecedence(char a, char b) {
        ArrayList<Character> p = new ArrayList<>();
        p.add('+');
        p.add('.');
        p.add('*');
        return p.indexOf(a) > p.indexOf(b);
    }
}

class RegexTree{
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




public class Main {
	

    public static String postfix(ArrayList<Character> alphabetSet, String regexp) {// 후위식 표기로 전환
        ArrayList<Character> temp = new ArrayList<>();
        for (int i = 0; i < regexp.length(); i++) {
        	//곱 연산 처리 
            if (i != 0
                    && (alphabetSet.contains(regexp.charAt(i - 1)) || regexp.charAt(i - 1) == ')' || regexp.charAt(i - 1) == Type.KLEENE.getText())
                    && (alphabetSet.contains(regexp.charAt(i))  || regexp.charAt(i) == '(')) {
                temp.add(Type.CONCAT.getText());
            }
            temp.add(regexp.charAt(i));
        }
        // regexp = temp.toString().replaceAll("[\\[\\], ]", "");
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
            } else if (stack.isEmpty() || stack.peek() == '(' || Type.higherPrecedence(c, stack.peek())) {
                stack.push(c);
            } else {
                while (!stack.isEmpty() && stack.peek() != '(' && !Type.higherPrecedence(c, stack.peek())) {
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
	
	public static RegexTree constructTree(ArrayList<Character> alphabet, String regexp) {
        Stack<RegexTree> stack = new Stack<>();
        for (char c : regexp.toCharArray()) {
            if (alphabet.contains(c)) {
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
	
	
	public static void main(String[] args) {
		/**
		 * [input]
		 * - a set of alphabet(ArrayList)
		 * - RegularExpression(String)
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		ArrayList<Character> alphabetSet = new ArrayList<>();
		
		System.out.println("alphabet을 입력하세요.(ex) A b c 입력 시 => {'A', 'b', 'c'}");
		String tempStr = sc.nextLine();
		for(String t : tempStr.split(" ")) {
			char c = t.charAt(0);
			if(alphabetSet.contains(t)) continue;
			alphabetSet.add(c);
		}
		
		System.out.println("Regular Expression을 입력하세요.");
		String regex = sc.nextLine();
		
		//알파벳이 아니거나 연산 기호가 아닌 것 전처리
		// System.out.println("alphabet이 아닌 문자가 입력되었습니다.\n Regular Expression을 다시 입력하세요.");
		// regex = sc.nextLine();

		
		String pRegex = postfix(alphabetSet, regex);
		System.out.println(pRegex);
		
		RegexTree rTree = constructTree(alphabetSet, pRegex);
		System.out.println(rTree.getValue());
		System.out.println(rTree.getLeft().getValue());
		System.out.println(rTree.getRight().getValue());
		
		/**
		 * 
		 * [function]
		 * - Regular Expression 연산 우선순위 구하기 
		 * - UNION(a|b), CONCAT(ab), KLEENE(a*, a+==aa*) 우선 순위에 따라 분리 및 연산 수행
		 * - Transition Tree 구축
		 * - Transition Tree에 따라 
		 * 
		 * [output]
		 * - states (Set)
		 * - transition function(ArrayList [][]) 
		 * - start state, final state
		 */
		
		
		
	}

}
