import java.util.Stack;

/**
 * @author leejiwon
 * 
 * RegularExpression to NFA-ュ
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
        
        startState.put('ュ', leftNFA);
    	startState.put('ュ', endState);
        leftEndState.put('ュ', endState);
        leftEndState.put('ュ', leftNFA);  
        
        es.push(endState);
    	return startState;
    }

    private NFAEState convertConcat(RegexTree rt, Stack<NFAEState> es) {
    	NFAEState leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	NFAEState leftEndState = es.pop();
    	NFAEState rightNFA = convertToFiniteAutomata(rt.getRight(), es);
        
        leftEndState.put('ュ', rightNFA);
        
        return leftNFA;
    }
    
    private NFAEState convertUnion(RegexTree rt, Stack<NFAEState> es) {
    	NFAEState startState = new NFAEState();
    	NFAEState endState = new NFAEState();
    	
    	NFAEState leftNFA = convertToFiniteAutomata(rt.getLeft(), es);
    	NFAEState leftEndState = es.pop();
    	NFAEState rightNFA = convertToFiniteAutomata(rt.getRight(), es);
    	NFAEState rightEndState = es.pop();
        
    	startState.put('ュ', leftNFA);
    	startState.put('ュ', rightNFA);
    	
    	leftEndState.put('ュ', endState);
    	rightEndState.put('ュ', endState);
    	
    	es.push(endState);
    	
    	return startState;
    }
	
}
