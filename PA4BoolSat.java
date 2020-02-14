package bool_exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PA4BoolSat {
    public static void main(String[] params) {

        checkUsage(params);

        String expr = getExpr(params);
        ASTNode root = BoolSatParser.parse(expr);

        List<String> idList = new ArrayList<String>();
        buildIdList(root, idList);
        Collections.sort(idList);

        Map<String, Boolean> id2Assignment = new TreeMap<String, Boolean>();

        Map<String, Boolean> configStr2Val = new TreeMap<String, Boolean>();
        evalAllConfigs(params, idList, configStr2Val, id2Assignment, root);

        printResults(params, expr, configStr2Val);




    }

    public static void checkUsage(String[] params) {
        if (params.length < 1 || params.length > 2) {
            System.err.println("USAGE ERROR");
            System.exit(1);
        }
    }

    public static String getExpr(String[] params) {
        String expr = null;
        String fName = params[0];
        try (Scanner s = new Scanner(new File(fName));) {
            expr = s.nextLine();
        } catch (FileNotFoundException e) {
            System.err.printf("'%s' is not a valid file path.", fName);
            System.exit(1);
        }

        return expr;
    }

    public static void buildIdList(ASTNode root, List<String> idList) {

        if (root.isId()) {
            String id = root.getId();
            idList.add(id);

        } else {
            buildIdList(root.child1, idList);
            buildIdList(root.child2, idList);
        }
    }


    public static void evalAllConfigs(String[] params,
            List<String> idList, Map<String, Boolean> configStr2Val,
            Map<String, Boolean> id2Assignment, ASTNode root) {

        // base case
        if (idList.isEmpty()) {
            Boolean exprTruthVal = evaluateExpr(root,
                    id2Assignment);
            String configStr = makeConfigStr(id2Assignment);
            configStr2Val.put(configStr, exprTruthVal);

            // recursive call
        } else {
            String id = idList.get(0);

            // choose true
            id2Assignment.put(id, true);
            // explore
            evalAllConfigs(params, idList.subList(1, idList.size()),
                    configStr2Val,
                    id2Assignment,
                    root);

            // choose false
            id2Assignment.put(id, false);
            // explore
            evalAllConfigs(params, idList.subList(1, idList.size()),
                    configStr2Val,
                    id2Assignment,
                    root);
        }

    }

    public static Boolean evaluateExpr(ASTNode root,
            Map<String, Boolean> id2Assignment) {
        if (root.isId()) {
            String id = root.getId();
            Boolean assignment = id2Assignment.get(id);
            return assignment;
        } else {
            Boolean child1TruthVal = evaluateExpr(root.child1,
                    id2Assignment);
            Boolean child2TruthVal = evaluateExpr(root.child2,
                    id2Assignment);
            return !(child1TruthVal && child2TruthVal);
        }
    }

    public static String makeConfigStr(Map<String, Boolean> id2Assignment) {

        String configStr = "";
        for (String id : id2Assignment.keySet()) {
            Boolean assignment = id2Assignment.get(id);
            configStr += String.format("%s: %b, ", id, assignment);
        }
        
        return configStr;
    }

    public static void printResults(String[] params, String expr,
            Map<String, Boolean> configStr2Val) {

        Boolean debugModeEnabled = (params.length == 2);
        String sat = (configStr2Val.values().contains(true) ? "SAT" : "UNSAT");


        System.out.println("input: " + expr);
        System.out.println(sat);
        if (sat.equals("UNSAT")) {
            return;
        }


        for (String configStr : configStr2Val.keySet()) {
            Boolean exprTruthVal = configStr2Val.get(configStr);
            if (debugModeEnabled) {
                System.out.println(configStr + exprTruthVal);
            } else if (exprTruthVal) {
                configStr = configStr.substring(0, configStr.length() - 2);
                System.out.println(configStr);
            }
        }


    }
}
