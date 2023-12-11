import java.util.Stack;

/**
 * @author leejiwon
 * 
 * RegularExpression to NFA-��
 * 
 */
public class NFAEConverter{
	public NFAEState convertToFiniteAutomata(RegexTree rt, Stack<NFAEState> es) {
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

    private NFAEState convertSymbol(RegexTree rt, Stack<NFAEState> es) {
        NFAEState startState = new NFAEState();
        NFAEState endState = new NFAEState();
        startState.put(rt.getValue(), endState);
        es.push(endState);
        return startState;
    }
    
    private NFAEState convertKleene(RegexTree rt, Stack<NFAEState> es) {
    	NFAEState startState = new NFAEState();
    	NFAEState endState = new NFAEState();
        
    	NFAEState leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	NFAEState leftEndState = es.pop();
        
        startState.put('��', leftNFA);
    	startState.put('��', endState);
        leftEndState.put('��', endState);
        leftEndState.put('��', leftNFA);  
        
        es.push(endState);
    	return startState;
    }

    private NFAEState convertConcat(RegexTree rt, Stack<NFAEState> es) {
    	NFAEState leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	NFAEState leftEndState = es.pop();
    	NFAEState rightNFA = convertToFiniteAutomata(rt.getRight(), es);
        
        leftEndState.put('��', rightNFA);
        
        return leftNFA;
    }
    
    private NFAEState convertUnion(RegexTree rt, Stack<NFAEState> es) {
    	NFAEState startState = new NFAEState();
    	NFAEState endState = new NFAEState();
    	
    	NFAEState leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	NFAEState leftEndState = es.pop();
    	NFAEState rightNFA = convertToFiniteAutomata(rt.getRight(), es);
    	NFAEState rightEndState = es.pop();
        
    	startState.put('��', leftNFA);
    	startState.put('��', rightNFA);
    	
    	leftEndState.put('��', endState);
    	rightEndState.put('��', endState);
    	
    	es.push(endState);
    	
    	return startState;
    }
	
}
