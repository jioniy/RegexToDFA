import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {
	
	public static void main(String[] args) {
		/**
		 * 1. ����� �Է�
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		List<Character> alphabetSet = new ArrayList<>();
		
		/*
		 * 1-1. ���ĺ� �Է�
		 * TODO alphabet ��ȿ�� �˻�(�����ڸ� ���ĺ����� �Է��� ���)
		 */
		System.out.println("alphabet�� �Է��ϼ���.(ex) A b c �Է� �� => {'A', 'b', 'c'}");
		String tempStr = sc.nextLine();
		for(String t : tempStr.split(" ")) {
			char c = t.charAt(0);
			if(alphabetSet.contains(c)) continue;
			alphabetSet.add(c);
		}
		
		/*
		 * 1-2. ���Խ� �Է�
		 * TODO RE ��ȿ�� �˻�(���ĺ��� �ƴ� ���, �������� ��ġ�� �������� ���)
		 */
		System.out.println("Regular Expression�� �Է��ϼ���.");
		String regex = sc.nextLine();

		
		/*
		 * 2. ���Խ� Ʈ�� ���� 
		 * 
		 */
		RegexTree rTree = RegexTreeConverter.convert(alphabetSet, regex);//RE TREE ����
		
		
		/*
		 * 3. ���ԽĿ� ���� NFA-�� ���ϱ�
		 */
		NFAEConverter nc = new NFAEConverter();
		
		Stack<NFAEState> endStateStack = new Stack<>();
		NFAEState startState = nc.convertToFiniteAutomata(rTree, endStateStack);//���ԽĿ� ���� ���丶Ÿ ��ȯ
		NFAEState finalState = endStateStack.pop();
		
		
		/*
		 * 4. NFA-��
		 * - Start, Final State
		 * - Transition Table
		 * 
		 */
		System.out.println("\n===================<RESULT>===================");
		System.out.println("\n> NFA-��");
		System.out.println("Start State : q"+startState.getID());
		System.out.println("Final State : q"+finalState.getID());
		System.out.println("Transition Table : ");
		NFAETransitionTable nt = new NFAETransitionTable(alphabetSet);
		nt.set(alphabetSet, startState);//transition�� table ����(2���� �迭)�� ����
		nt.print(alphabetSet);//���
		
		
		/**
		 * 5. NFA-�� to DFA
		 * - ��-closure 
		 * - Transition table
		 */
		System.out.println("\n> ��-closure");
		
		System.out.println("\n> DFA");
		
		
		
		/**
		 * 6. ���ڿ� Ȯ��
		 * ���ڿ� �Է� 
		 * accept / reject
		 */
		System.out.println("\n===================<CHECK>===================");
		System.out.println("Ȯ���ϰ� ���� ���ڿ��� �Է��ϼ���.");
		String inputStr = sc.nextLine();
		
		
	}

}
