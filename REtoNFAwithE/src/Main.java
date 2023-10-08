import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/*
 * 문자 타입
 * SYMBOL - alphabet
 * CONCAT
 * UNION
 * KLEENE
 */
enum Type{
	SYMBOL(' ', -1),
	CONCAT('.', 1),
	UNION('+', 2),
	KLEENE('*', 3); // priority 값이 높을 수록 우선임. 
	
	private final Character text;
	private final int priority;
	
	Type(Character text, int priority){
		this.text = text;
		this.priority = priority;
	}
	
	public Character getText() {
		return text;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public static Type findTypeByText(Character text) {
		return Arrays.stream(values())
				.filter(type -> type.text == text)
				.findAny()
				.orElse(null);
		
	}
	public static boolean isHigherPriority(char a, char b) {
        int ap = findTypeByText(a).priority;//a 우선순위
        int bp = findTypeByText(b).priority;//b 우선순위
        
        return ap > bp;
    }
}

/*
 * Regular Expression Tree
 */
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

/*
 * State of Finite State Automata
 */
class State {
    private Map<Character, List<State>> nextState;
    
    public State() {
        nextState = new HashMap<>();
    }

    public void put(char symbol, State state) {
        nextState.putIfAbsent(symbol, new ArrayList<>());//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
        nextState.get(symbol).add(state);
    }

    public List<State> get(char symbol) {
        return nextState.getOrDefault(symbol, new ArrayList<>());// 찾는 key가 존재하면 -> 해딩값을 반환, 그렇지 않으면 디폴트 값 반환
    }
    
}

/*
 * 유한 오토마타 변환기
 */
class FiniteAutomataConverter{
	public State convertToFiniteAutomata(RegexTree rt) {
        if (rt.getType() == Type.SYMBOL) {
            return convertSymbol(rt);
        } else if (rt.getType() == Type.CONCAT) {
            return convertConcat(rt);
        } else if (rt.getType() == Type.UNION) {
            return convertUnion(rt);
        } else if (rt.getType() == Type.KLEENE) {
            return convertKleene(rt);
        }
        throw new IllegalArgumentException("Invalid expression type");
    }

    private State convertSymbol(RegexTree rt) {
        State startState = new State();
        State endState = new State();
        startState.put(rt.getValue(), endState);
        return startState;
    }
    
    private State convertKleene(RegexTree rt) {
    	State startState = new State();
        State endState = new State();
        
        State leftNFA = convertToFiniteAutomata(rt.getLeft());
        
        startState.put('ε', leftNFA);
    	startState.put('ε', endState);
        leftNFA.put('ε', endState);
        
        //how to find final state of left NFA ?
        
    	return startState;
    }

    private State convertConcat(RegexTree rt) {
        State leftNFA = convertToFiniteAutomata(rt.getLeft());
        State rightNFA = convertToFiniteAutomata(rt.getRight());
        leftNFA.put('ε', rightNFA);//TODO
        return leftNFA;
    }
    
    private State convertUnion(RegexTree rt) {
    	State startState = new State();
    	State endState = new State();
    	
    	State leftNFA = convertToFiniteAutomata(rt.getLeft());
        State rightNFA = convertToFiniteAutomata(rt.getRight());
        
        
    	startState.put('ε', leftNFA);
    	startState.put('ε', rightNFA);
    	
    	leftNFA.put('ε', endState);
    	rightNFA.put('ε', endState);
    	
    	return startState;
    }
	
}



public class Main {

    public static String postfix(List<Character> alphabetSet, String regexp) {// RE 후위식 표기로 전환
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
	
	public static RegexTree constructTree(List<Character> alphabetSet, String regexp) {//RegexTree 구성
        Stack<RegexTree> stack = new Stack<>();
        for (char c : regexp.toCharArray()) {
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
	
	
	public static void inorder(RegexTree rt) { // RegexTree 중위순회 - check 용
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
	
	
	public static void main(String[] args) {
		/**
		 * [input]
		 * - a set of alphabet(ArrayList<Character>)
		 * - RegularExpression(String)
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		List<Character> alphabetSet = new ArrayList<>();
		
		//알파벳 입력
		System.out.println("alphabet을 입력하세요.(ex) A b c 입력 시 => {'A', 'b', 'c'}");
		String tempStr = sc.nextLine();
		for(String t : tempStr.split(" ")) {
			char c = t.charAt(0);
			if(alphabetSet.contains(c)) continue;
			alphabetSet.add(c);
		}
		
		
		//RE 입력
		System.out.println("Regular Expression을 입력하세요.");
		String regex = sc.nextLine();
		

		//TODO RE 유효성 검사
		//알파벳이 아니거나 연산 기호가 아닌 것 등 전처리
		// System.out.println("alphabet이 아닌 문자가 입력되었습니다.\n Regular Expression을 다시 입력하세요.");
		// regex = sc.nextLine();

		
		//RE 후위식 변환
		String pRegex = postfix(alphabetSet, regex);
		System.out.println(pRegex);
		
		//RE TREE 구성
		RegexTree rTree = constructTree(alphabetSet, pRegex);
		
		//CHECK RE TREE
		inorder(rTree);
		
		
		/**
		 * 
		 * [function]
		 * - UNION(a|b), CONCAT(ab), KLEENE(a*, a+==aa*) 우선 순위에 따라 분리하여 연산 수행
		 */
		FiniteAutomataConverter fac = new FiniteAutomataConverter();
		State startState = fac.convertToFiniteAutomata(rTree);
		
		
		/*
		 * [output]
		 * - states
		 * - transition function
		 * - start state, final state
		 */
		
		
	}

}
