import java.util.HashMap;

import com.biotools.meerkat.Action;

public class ActionConverter {

    private final static HashMap<Integer, String> actionNames = new HashMap<Integer, String> () {
        
		private static final long serialVersionUID = -816882655436816817L;

		{
            put(Action.ALLIN_PASS,      "allinPass");
            put(Action.BET,             "bet");
            put(Action.BIG_BLIND,       "bigBlind");
            put(Action.CALL,            "call");
            put(Action.CHECK,           "check");
            put(Action.FOLD,            "fold");
            put(Action.INVALID,         "invalid");
            put(Action.MUCK,            "muck");
            put(Action.POST_ANTE,       "postAnte");
            put(Action.POST_BLIND,      "postBlind");
            put(Action.POST_DEAD_BLIND, "postDeadBlind");
            put(Action.RAISE,           "raise");
            put(Action.SIT_OUT,         "sitOut");
            put(Action.SMALL_BLIND,     "smallBlind");
        }
    };

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
        return actionNames.get(action.getType());
    }

}
