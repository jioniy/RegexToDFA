import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class DFAConverter {
	private Set<DFAState> DFAStates;
	// NFA transition으로 갈 수 있는 state 집합 -> DFA state로 생성
	//1) start state의 epsilon closure를 DFA State로 설정
	//2) 해당 DFA State에 대한 각 알파벳 마다의 transition 구하기 -> Set으로 Add
	
	public DFAConverter() {
		DFAStates = new HashSet<>();
	}
	
	public DFAState convertToDFA(NFAEState currState, Map<Integer, Set<NFAEState>> epsilonClosureSet) {
		
		Set<NFAEState> currStates = new HashSet<>();
		currStates.add(currState);
		
		DFAState state = convertToDFA(currStates, epsilonClosureSet);
		
		return state;	
	}
	
	
	
	public DFAState convertToDFA(Set<NFAEState> currStates, Map<Integer, Set<NFAEState>> epsilonClosureSet) { //  NFAEState[] NFAEStateList, 
		
		//(1)curr states의 epsilon closure 가져오기 
		Set<NFAEState> startStates = new HashSet<>();
		
		for(NFAEState ns : currStates) {
			startStates.addAll(epsilonClosureSet.get(ns.getID()));
		}
		
		DFAState state = new DFAState(startStates);
		
		//기존 DFA State에 존재하는지 검사 -> 존재한다면 해당 DFA state 반환
		Iterator<DFAState> sIter1 = DFAStates.iterator();
		while(sIter1.hasNext()) {
			DFAState ds = sIter1.next();
			if(ds.equals(state)) return ds;
		}
		
		DFAState.plusStateCount();
		DFAStates.add(state);
		
		// (2)각 state에 대한 transition의 결과로 state모음
		Iterator<NFAEState> sIter2 = startStates.iterator();
		while(sIter2.hasNext()) {
			NFAEState ns = sIter2.next();
			for(Map.Entry<Character, List<NFAEState>> entry : ns.getNextStates().entrySet()) {
				if(entry.getKey().equals('ε'))continue;
				state.putTempNextStates(entry.getKey(), entry.getValue());
			}
		}
		
		
		// (3)state모음  + 결과의 epsilon transition 포함 => 최종 transition으로 확정 
		for(Map.Entry<Character, Set<NFAEState>> entry : state.getTempNextStates().entrySet()) {
			Set<NFAEState> endStates = new HashSet<>();
			
			for(NFAEState ns : entry.getValue()) {
				endStates.addAll(epsilonClosureSet.get(ns.getID()));
			}
			state.putNextState(entry.getKey(), convertToDFA(endStates,epsilonClosureSet));
		}
		
		return state;	
	}
	
	public void printDFAStates() {
		Iterator<DFAState> sIter1 = DFAStates.iterator();
		while(sIter1.hasNext()) {
			DFAState ds = sIter1.next();
			Iterator<NFAEState> sIter2 = ds.getNFAStateSet().iterator();
			System.out.print("Q"+ds.getID()+"={");
			while(sIter2.hasNext()) {
				System.out.print("q"+sIter2.next().getID()+",");
			}
			System.out.println("}");
		}
	}
	
	public void printDFAAcceptingStates(NFAEState finalState) {
		Iterator<DFAState> sIter1 = DFAStates.iterator();
		while(sIter1.hasNext()) {
			DFAState ds = sIter1.next();
			Iterator<NFAEState> sIter2 = ds.getNFAStateSet().iterator();
			while(sIter2.hasNext()) {
				if(finalState.equals(sIter2.next()))System.out.print("Q"+ds.getID()+", ");
			}
		}
	}

	
	
}
