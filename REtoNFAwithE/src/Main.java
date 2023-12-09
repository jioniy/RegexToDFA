import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Expression 타입
 * SYMBOL - alphabet
 * CONCAT
 * UNION
 * KLEENE
 */
enum Type{
	SYMBOL(' ', 0),
	UNION('+', 1),
	CONCAT('.', 2),
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
	private static int stateCnt = 0; // 총 State갯수 -> 식별자 생성에 이용
	private int ID;
    private Map<Character, List<State>> nextStates;
    
    
    public State() {
    	ID = stateCnt;
        nextStates = new HashMap<>();
        stateCnt++;
    }
    
    public static int getStateCount() {
    	return stateCnt;
    }

    public int getID() {
    	return ID;
    }
    
    public Map<Character, List<State>> getNextStates(){
    	return nextStates;
    }
    
    public void put(char symbol, State state) {
        nextStates.putIfAbsent(symbol, new ArrayList<>());//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
        nextStates.get(symbol).add(state);
    }
}

/*
 * 유한 오토마타 변환기 (Regex to NFA-ε)
 */
class FiniteAutomataConverter{
	public State convertToFiniteAutomata(RegexTree rt, Stack<State> es) {
        if (rt.getType() == Type.SYMBOL) {
            return convertSymbol(rt, es);
        } else if (rt.getType() == Type.CONCAT) {
            return convertConcat(rt, es);
        } else if (rt.getType() == Type.UNION) {
            return convertUnion(rt, es);
        } else if (rt.getType() == Type.KLEENE) {
            return convertKleene(rt, es);
        }
        throw new IllegalArgumentException("Invalid expression type");
    }

    private State convertSymbol(RegexTree rt, Stack<State> es) {
        State startState = new State();
        State endState = new State();
        startState.put(rt.getValue(), endState);
        es.push(endState);
        return startState;
    }
    
    private State convertKleene(RegexTree rt, Stack<State> es) {
    	State startState = new State();
        State endState = new State();
        
        State leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
        State leftEndState = es.pop();
        
        startState.put('ε', leftNFA);
    	startState.put('ε', endState);
        leftEndState.put('ε', endState);
        leftEndState.put('ε', leftNFA);  
        
        es.push(endState);
    	return startState;
    }

    private State convertConcat(RegexTree rt, Stack<State> es) {
        State leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
        State leftEndState = es.pop();
        State rightNFA = convertToFiniteAutomata(rt.getRight(), es);
        
        leftEndState.put('ε', rightNFA);
        
        return leftNFA;
    }
    
    private State convertUnion(RegexTree rt, Stack<State> es) {
    	State startState = new State();
    	State endState = new State();
    	
    	State leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	State leftEndState = es.pop();
        State rightNFA = convertToFiniteAutomata(rt.getRight(), es);
        State rightEndState = es.pop();
        
    	startState.put('ε', leftNFA);
    	startState.put('ε', rightNFA);
    	
    	leftEndState.put('ε', endState);
    	rightEndState.put('ε', endState);
    	
    	es.push(endState);
    	
    	return startState;
    }
	
}

/**
 * 
 * transition table 구성 및 출력
 *
 */
class FiniteAutomataTable{
	private String[][] data;
	
	public FiniteAutomataTable(List<Character> symbols) {
		symbols.add('ε');
		
		data = new String[State.getStateCount()][symbols.size()];

	}
	
	public void set(List<Character> symbols, State state){
		
		for(Map.Entry<Character, List<State>> entry : state.getNextStates().entrySet()) {
			Character symbol = entry.getKey();
			List<State> nextStates = entry.getValue();
			
			String nextStateStr = "{";
			
			for(State ns : nextStates) {
				nextStateStr += ("q"+Integer.toString(ns.getID())+",");
			}	
			nextStateStr += "}";
			
			data[state.getID()][symbols.indexOf(symbol)] = nextStateStr;
			
			for(State ns : nextStates) {
				if(!(String.join("",data[ns.getID()]).contains("{")&&String.join("",data[ns.getID()]).contains("}"))) {
					set(symbols, ns);
				}
			}
		}
		
	}
	
	public void print(List<Character> symbols) {
		System.out.print("     ");
		//print symbols
		for(int i = 0; i<symbols.size();i++) {
			System.out.print("     "+symbols.get(i)+"      ");
		}
		
		System.out.println();
		// System.out.println("-".repeat(symbols.size()*12+7));
		for(int i =0 ;i<symbols.size()*12+7;i++) {
			System.out.print("-");
		}
		System.out.println();
		
		
		for(int i = 0; i<data.length;i++) {
			System.out.print("q"+i+" : ");
			for(int j = 0; j<data[i].length; j++) {
				String nextStr = (data[i][j] != null)? data[i][j] : "---";
				System.out.print("    "+nextStr+"    ");
			}
			System.out.println();
		}
	}
}


/**
 * TODO
 * 입력 유효성 검사기 (알파젯, 정규식 등)
 *
 */
class InputValidator{
	public static boolean isValidAlphabetSet(List<Character> alphabetSet, String message) {
		if(alphabetSet.contains('(')||alphabetSet.contains(')')||alphabetSet.contains('*')||alphabetSet.contains('+')) {
			message = "연산자는 알파벳으로 사용할 수 없습니다.";
			return false;
		}
		return true;
	}
	//public static boolean isValidRegex(List<Character> alphabetSet, String regex, String message);
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
	
	public static RegexTree constructTree(List<Character> alphabetSet, String pRegexp) {//RegexTree 구성
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
		 * 1. 사용자 입력
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		List<Character> alphabetSet = new ArrayList<>();
		
		/*
		 * 1-1. 알파벳 입력
		 * TODO alphabet 유효성 검사(연산자를 알파벳으로 입력한 경우)
		 */
		System.out.println("alphabet을 입력하세요.(ex) A b c 입력 시 => {'A', 'b', 'c'}");
		String tempStr = sc.nextLine();
		for(String t : tempStr.split(" ")) {
			char c = t.charAt(0);
			if(alphabetSet.contains(c)) continue;
			alphabetSet.add(c);
		}
		
		/*
		 * 1-2. 정규식 입력
		 * TODO RE 유효성 검사(알파벳이 아닌 경우, 연산자의 위치가 부적절한 경우)
		 */
		System.out.println("Regular Expression을 입력하세요.");
		String regex = sc.nextLine();

		
		
		
		/*
		 * 2. 겅규식 트리 구성 
		 * 
		 */
		String pRegex = postfix(alphabetSet, regex);//RE 후위식 변환
		// CHECK postfix => System.out.println("RE 후위식 변환 : " + pRegex);

		RegexTree rTree = constructTree(alphabetSet, pRegex);//RE TREE 구성
		//CHECK RE TREE => inorder(rTree);
		
		
		
		/*
		 * 3. 정규식에 대한 NFA-ε 구하기
		 */
		FiniteAutomataConverter fac = new FiniteAutomataConverter();
		
		Stack<State> endStateStack = new Stack<>();
		State startState = fac.convertToFiniteAutomata(rTree, endStateStack);//정규식에 대한 오토마타 변환
		State finalState = endStateStack.pop();
		
		
		
		
		/*
		 * 4. 출력
		 * - Start, Final State
		 * - Transition Table
		 * 
		 */
		System.out.println("\n===================<RESULT>===================");
		System.out.println("Start State : q"+startState.getID());
		System.out.println("Final State : q"+finalState.getID());
		System.out.println("Transition Table : ");
		FiniteAutomataTable fat = new FiniteAutomataTable(alphabetSet);
		fat.set(alphabetSet, startState);//transition을 table 형태(2차원 배열)로 구성
		fat.print(alphabetSet);//출력
		
		/**
		 * 5. 문자열 확인
		 * NFA-e -> NFA -> DFA 
		 * 문자열 입력 
		 * accept / reject
		 */
		System.out.println("\n===================<CHECK>===================");
		System.out.println("확인하고 싶은 문자열을 입력하세요.");
		String inputStr = sc.nextLine();
		
		
		
	}

}
