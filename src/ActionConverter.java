import com.biotools.meerkat.Action;

public class ActionConverter {

    public static Action stringToAction (String actionString, double toCall, double raiseAmount) {
        if(actionString.equals("fold")) {
         return Action.foldAction(toCall);
        } else if(actionString.equals("call") || actionString.equals("check")) {
         return Action.callAction(toCall);
        } else if(actionString.equals("raise")) {
         return Action.raiseAction(toCall, raiseAmount);
        }
        return new Action(-1, 0, 0);
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
