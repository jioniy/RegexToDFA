

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author leejiwon
 *
 * State of NFA-��
 *  
 */
public class NFAEState {
	private static int stateCnt = 0; // �� State���� -> �ĺ��� ������ �̿�
	private int ID;
    private Map<Character, List<NFAEState>> nextStates;
    
    
    public NFAEState() {
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
    
    public Map<Character, List<NFAEState>> getNextStates(){
    	return nextStates;
    }
    
    public void put(char symbol, NFAEState state) {
        nextStates.putIfAbsent(symbol, new ArrayList<>());//key���� �������� ������ -> key�� value�� Map�� �����ϰ� null�� ��ȯ
        nextStates.get(symbol).add(state);
    }
}