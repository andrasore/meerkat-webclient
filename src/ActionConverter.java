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
    
    
    public static String toString (Action action) {
        if (action.isBet()) {
        	return "bet";
        } else if (action.isRaise()) {
        	return "raise";
        } else if (action.isCall()) {
        	return "call";
        } else if (action.isCheck()) {
        	return "check";
        } else if (action.isFold()) {
        	return "fold";
        } else {
        	return "invalid";
        }
    }
}
