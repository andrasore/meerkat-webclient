import com.biotools.meerkat.Action;

public class ActionConverter {

    public static int stringToInt (String actionString) {
        if(actionString.equals("fold")) {
         return Action.FOLD;
        } else if(actionString.equals("check")) {
         return Action.CHECK;
        } else if(actionString.equals("raise")) {
         return Action.RAISE;
        }
        return Action.INVALID;
    }

    
    public static String intToString (int actionInt) {
    	switch (actionInt) {
    		case Action.ALLIN_PASS:
    			return "allin";
    		case Action.BET:
    			return "bet";
    		case Action.BIG_BLIND:
    			return "big blind";
    		case Action.CALL:
    			return "call";
    		case Action.CHECK:
    			return "check";
    		case Action.FOLD:
    			return "fold";
    		case Action.MUCK:
    			return "muck";
    		case Action.POST_ANTE:
    			return "post ante";
    		case Action.POST_BLIND:
    			return "post blind";
    		case Action.POST_DEAD_BLIND:
    			return "post dead blind";
    		case Action.RAISE:
    			return "raise";
    		case Action.SIT_OUT:
    			return "sit out";
    		case Action.SMALL_BLIND:
    			return "small blind";
    	    default: 
    	    	return "invalid";
    	}
    }
}
