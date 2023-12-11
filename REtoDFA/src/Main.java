import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
/**
 * 
 * @author jioniy
 * 
 * TODO 
 * - 알파벳 & 정규식 유효성 검사
 * - DFA 변환 Test 필요
 * 
 */
public class Main {
	
	public static void main(String[] args) {
		/**
		 * 1. 사용자 입력
		 * 
		 */
		System.out.println("===================[프로그램 시작]===================");
		System.out.println();
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
		 * 2. 정규식 트리 구성 
		 * 
		 */
		RegexTree rTree = RegexTreeConverter.convert(alphabetSet, regex);//RE TREE 구성
		
		
		/*
		 * 3. 정규식에 대한 NFA-ε 구하기
		 */
		NFAEConverter nc = new NFAEConverter();
		
		Stack<NFAEState> endStateStack = new Stack<>();
		NFAEState startState = nc.convertToFiniteAutomata(rTree, endStateStack);//정규식에 대한 오토마타 변환
		NFAEState finalState = endStateStack.pop();
		
		
		/*
		 * 4. NFA-ε
		 * - Start, Final State
		 * - Transition Table
		 * 
		 */
		System.out.println("\n===================<RESULT>===================");
		System.out.println("\n===> NFA-ε");
		System.out.println("Start State : q"+startState.getID());
		System.out.println("Final State : q"+finalState.getID());
		System.out.println("Transition Table : ");
		NFAETransitionTable nt = new NFAETransitionTable(alphabetSet);
		nt.set(startState);//transition을 table 형태(2차원 배열)로 구성
		nt.print();//출력
		
		
		/**
		 * 5. NFA-ε to DFA
		 * - ε-closure 
		 * - Transition table
		 * TODO 다른 정규식에 대한 확인 필요
		 */
		System.out.println();
		System.out.println("\n===> ε-closure");
		nt.setEpsilonClosure();
		nt.printEpsilonClosure();
		
		
		System.out.println();
		System.out.println("\n===> DFA");
		DFAConverter dc = new DFAConverter();
		
		DFAState dfa = dc.convertToDFA(startState, nt.getEpsilonClosureSet());
		System.out.println("States : ");
		dc.printDFAStates();
		System.out.println("\nAccepting States : ");
		dc.printDFAAcceptingStates(finalState);
		
		System.out.println("\n\nTransition Table : ");
		DFATransitionTable dt = new DFATransitionTable(alphabetSet);
		dt.set(dfa);
		dt.print();
		
		
		
		/**
		 * 
		 * 6. 문자열 확인
		 * 문자열 입력 
		 * accept / reject
		 */
		
		System.out.println();
		System.out.println("\n===================<CHECK>===================");
		
		DeterministicAccepter da = new DeterministicAccepter(dfa, finalState);
		
		System.out.println("확인하고 싶은 문자열을 입력하세요. \':q\'를 입력하면 종료됩니다.");
		String inputStr = "";
		
		
		while(true){
			System.out.print(">");
			
			inputStr = sc.nextLine();
			if(inputStr.equals(":q")) {
				System.out.println();
				System.out.println("===================[프로그램 종료]===================");
				break;
			}
			if(da.isAccepted(inputStr)) System.out.println("Accepted! 언어에 해당하는 문자열입니다.");
			else System.out.println("Rejected! 언어에 해당하지 않는 문자열입니다.");
		}
		
		
		
	}

}
