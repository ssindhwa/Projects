// This program will plot a CFG for a method using soot
// [ExceptionalUnitGraph feature].
// Arguements : <ProcessOrTargetDirectory> <MainClass> <TargetClass> <TargetMethod>

// Ref:
// 1) https://gist.github.com/bdqnghi/9d8d990b29caeb4e5157d7df35e083ce
// 2) https://github.com/soot-oss/soot/wiki/Tutorials

////////////////////////////////////////////////////////////////////////////////
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////

import soot.options.Options;

import soot.Unit;
import soot.Scene;
import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.UnitPrinter;
import soot.NormalUnitPrinter;

import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.ArrayRef;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import soot.Local;

//import com.azul.crs.client.Result;

////////////////////////////////////////////////////////////////////////////////

public class Analysis extends PAVBase {
    private DotGraph dot = new DotGraph("callgraph");
    private static HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    private static List<SootMethod> func_List = new ArrayList<>();
    private static HashMap<SootMethod,Integer> method_to_size = new HashMap();
    public static HashMap<SootMethod,List<Map.Entry<LatticeElement, Boolean>> > method_progPoint = new HashMap();
    public static HashMap<Unit, Integer> ir_lines = new HashMap<>();
    //private static SootClass static_class;
    
    public Analysis() {
    }

    public static void doAnalysis(SootMethod targetMethod, List<SootMethod> targetClassMethods){
	/*************************************************************
         * XXX you can implement your analysis here
         ************************************************************/

    }
    public static void initialize_progPoint(SootMethod targetMethod,Integer sz){
        List<Map.Entry<LatticeElement, Boolean>> local_progPoint = new ArrayList<>();
        for(int i=0; i<sz; i++){
            My_lattice_element new_obj = new My_lattice_element();
            new_obj.lat_ele.put("@",new HashMap<>());
            local_progPoint.add(new AbstractMap.SimpleEntry<>(new_obj, true));
        }
        method_progPoint.put(targetMethod,local_progPoint);
    }
    public static void print_progP(String method_name, String class_name,BufferedWriter writer){
       

      
    }
    public static Set<String> Kildalls(SootMethod targetMethod){
       
        Set<String> set1 = new HashSet<>();
       
        
        for(HashMap.Entry<SootMethod,List<Map.Entry<LatticeElement, Boolean>> > e: method_progPoint.entrySet()){
            System.out.println(e.getKey()+" "+method_to_size.get(e.getKey()));
            for(int i=0; i<method_to_size.get(e.getKey());i++){
                My_lattice_element le = (My_lattice_element)e.getValue().get(i).getKey();
                
                System.out.println(le.lat_ele);
                for (Map.Entry<String, HashMap<String, Set<String>>> outerEntry : le.lat_ele.entrySet()) {
                    String outerKey = outerEntry.getKey();
                    HashMap<String, Set<String>> innerHashMap = outerEntry.getValue();
                    
                    // Iterate over the inner HashMap
                    for (Map.Entry<String, Set<String>> innerEntry : innerHashMap.entrySet()) {
                        String innerKey = innerEntry.getKey();
                        Set<String> innerSet = innerEntry.getValue();

                        // Iterate over the Set
                        for (String value : innerSet) {
                            // Access each value in the Set
                            System.out.println("CallString: " + outerKey +
                                    ", Variable: " + innerKey +
                                    ", Value: " + value);
                        }
                    }
                }
            }
        }
      

        if (!targetMethod.isPhantom() && targetMethod.isConcrete())
        {
            Body body = targetMethod.retrieveActiveBody();


            ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);
            // String file_path = output_dir+"/"+method_name+"_killdal_output.txt";
            // BufferedWriter writer = null;
            // try {
                // writer = new BufferedWriter(new FileWriter(file_path));
                int iteration = 1;
                while(true && iteration<10){
                    /* To check if any marked ProgPoint exist */
                    int mark_in = -1;
                    for(int i =0; i<body.getUnits().size(); i++ ){
                        if(method_progPoint.get(targetMethod).get(i).getValue()){
                            mark_in = i;
                            /* found Marked Point, break */
                            break;
                        }
                    } 
                    if ( mark_in == -1)
                        break;

                   
                            Stmt cur_st = (Stmt)My_lattice_element.Name2CFG.get(targetMethod).get(String.format("%02d", mark_in));

                            LatticeElement marked_ele = method_progPoint.get(targetMethod).get(mark_in).getKey();
                            List<Map.Entry<LatticeElement, Boolean>> list_idx_mark_le = method_progPoint.get(targetMethod);
                            list_idx_mark_le.set(mark_in, new AbstractMap.SimpleEntry<>(marked_ele, false));
                            method_progPoint.put(targetMethod, list_idx_mark_le);

                           
                            if(cur_st instanceof AssignStmt){
                                System.out.println(" --- cur_st: "+cur_st+" "+((My_lattice_element)marked_ele).lat_ele);
                                LatticeElement returned_le = marked_ele.tf_assignstmt(cur_st);
                                System.out.println(((My_lattice_element)returned_le).lat_ele);
                                List<Unit> succs_Of = graph.getSuccsOf((Unit)cur_st);
                                if(succs_Of.size() != 0){
                                    HashMap<String, Unit> lines = My_lattice_element.Name2CFG.get(targetMethod);
                                    String succ_indx_str="-1";
                                    for(HashMap.Entry<String, Unit> l : lines.entrySet()){
                                        if(((Stmt)l.getValue()).equals(succs_Of.get(0))){
                                            succ_indx_str = l.getKey();
                                            break;
                                        }                        
                                    }
                                    if(succ_indx_str=="-1")
                                        continue;
                                    int succ_indx = Integer.parseInt(succ_indx_str);
                                    LatticeElement ele3 = returned_le.join_op(method_progPoint.get(targetMethod).get(succ_indx).getKey());
                                    
                                    /* If not same then mark as true */
                                    if(ele3.equals(method_progPoint.get(targetMethod).get(succ_indx).getKey()) == false){
                                        List<Map.Entry<LatticeElement, Boolean>> cp_list = new ArrayList<>();

                                        for (Map.Entry<LatticeElement, Boolean> e : method_progPoint.get(targetMethod)) {
                                            
                                            Map.Entry<LatticeElement, Boolean> e2 = new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue());
                                            cp_list.add(e2);
                                        }
                                        cp_list.set(succ_indx, new AbstractMap.SimpleEntry<>(ele3, true));
                                        method_progPoint.put(targetMethod,cp_list);
                                    }
                                    List<Map.Entry<LatticeElement, Boolean>> cp_list = new ArrayList<>();

                                    for (Map.Entry<LatticeElement, Boolean> e : method_progPoint.get(targetMethod)) {
                                        
                                        Map.Entry<LatticeElement, Boolean> e2 = new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue());
                                        cp_list.add(e2);
                                    }
                                    cp_list.set(succ_indx, new AbstractMap.SimpleEntry<>(ele3, true));
                                    method_progPoint.put(targetMethod,cp_list); 
                                }
                               
                                  
                            }
                           
                            else{
                                List<Unit> succs_Of = graph.getSuccsOf((Unit)cur_st);
                                for(int i=0; i<succs_Of.size(); i++){
                                    HashMap<String, Unit> ir = My_lattice_element.Name2CFG.get(targetMethod);
                                    String succ_indx="-1";
                                    for(HashMap.Entry<String, Unit> st_ir: ir.entrySet()){
                                        if(((Stmt)st_ir.getValue()).equals(succs_Of.get(i))){
                                            succ_indx = st_ir.getKey();
                                            break;
                                        }                        
                                    }
                                    if(succ_indx=="-1")
                                        continue;
                                    int succ = Integer.parseInt(succ_indx);
                                    LatticeElement ele3 = marked_ele.join_op(method_progPoint.get(targetMethod).get(succ).getKey());
                                   
                                    if(ele3.equals(method_progPoint.get(targetMethod).get(succ).getKey()) == false){
                                        List<Map.Entry<LatticeElement, Boolean>> cp_list = new ArrayList<>();

                                        for (Map.Entry<LatticeElement, Boolean> e : method_progPoint.get(targetMethod)) {
                                           
                                            Map.Entry<LatticeElement, Boolean> e2 = new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue());
                                            cp_list.add(e2);
                                        }
                                        cp_list.set(succ, new AbstractMap.SimpleEntry<>(ele3, true));
                                        method_progPoint.put(targetMethod,cp_list);
                                    }
                                    
                                }
                                
                                List<Map.Entry<LatticeElement, Boolean>> cp_list = new ArrayList<>();

                                for (Map.Entry<LatticeElement, Boolean> e : method_progPoint.get(targetMethod)) {
                                   
                                    Map.Entry<LatticeElement, Boolean> e2 = new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue());
                                    cp_list.add(e2);
                                }
                                cp_list.set(mark_in, new AbstractMap.SimpleEntry<>(marked_ele, false));
                                method_progPoint.put(targetMethod,cp_list);
                            }
                       
                    iteration++; // increase iteration    
                }
                            
                        
                    // }
                    // print_progP(method_name, class_name,writer);
                    // writer.newLine(); /* Extra blank line to separate the Iteration from previous Iteration */
               
            // } catch (IOException e) {
            //     System.err.println("Error appending data to the file: "+ file_path + ", error msg :" + e.getMessage());
            // }finally {
            //     try {
            //         // Close the BufferedWriter (if it has been initialized)
            //         if (writer != null) {
            //             writer.close();
            //             System.out.println("File "+file_path+" closed successfully.");
            //         }
            //     } catch (IOException e) {
            //         System.err.println("Error closing the file "+file_path+": " + e.getMessage());
            //     }
            // }
        }
        return set1;
    }

    public static void getJimpleBody(){
       
        while(!func_List.isEmpty()){
            if(!My_lattice_element.Name2CFG.containsKey(func_List.get(0).toString())){
                if (!func_List.get(0).isPhantom() && func_List.get(0).isConcrete())
                {
                    Body body = func_List.get(0).retrieveActiveBody();
                    List<Local> locals = body.getParameterLocals();
                    System.out.println(func_List.get(0).toString());
                    System.out.println(locals);


                    HashMap<String, Unit> call_str_ir = new HashMap<>(); 

                    int lineno = 0;
                   
                    for (Unit u : body.getUnits()) {
                        if (!(u instanceof Stmt)) {
                            continue;
                        }
                        Stmt s = (Stmt) u;
                       

                        if (s.containsInvokeExpr()) {
                        InvokeExpr ie = s.getInvokeExpr();
                            if (ie instanceof StaticInvokeExpr) {
       
     


                                SootMethod calledMethod = ie.getMethod();
                               // System.out.println(" Called Method ");
                                //System.out.println(calledMethod.getName());
                                if(!func_List.contains(calledMethod)){
                                    // printInfo(calledMethod);
                                String key = func_List.get(0).toString()+".in"+ String.format("%02d", lineno);
                                My_lattice_element.Call2CFG.put(key, calledMethod);
                                func_List.add(calledMethod);
                                }
                            }
                        }
                        call_str_ir.put(String.format("%02d", lineno), u);
                        lineno++;
                    }
                    method_to_size.put(func_List.get(0),lineno);
                    My_lattice_element.Name2CFG.put(func_List.get(0),call_str_ir);
                }
                
            }
            // System.out.println("func_List");
            // System.out.println(func_List);
            
            // System.out.println(func_List);
            System.out.println("Call2CFG");
            System.out.println(My_lattice_element.Call2CFG);
            System.out.println("Name2CFG");
            System.out.println(My_lattice_element.Name2CFG);
            for (HashMap.Entry<SootMethod, HashMap<String, Unit>> e : My_lattice_element.Name2CFG.entrySet()) {
                System.out.println(e.getKey().getName());
                if(e.getKey().getName().equals(func_List.get(0).toString()))
                for(HashMap.Entry<String, Unit> val : e.getValue().entrySet()){
                    System.out.print(val.getKey()+" ");
                    System.out.println(val.getValue());                
                }
            }
            func_List.remove(0);
        }
        
        // Initialize the Table of Kildall
        for (HashMap.Entry<SootMethod, Integer> entry : method_to_size.entrySet()) {
            initialize_progPoint(entry.getKey(),entry.getValue());
        }

    }


    public static void main(String[] args) {

        String targetDirectory=args[0];
        String mClass=args[1];
        String tClass=args[2];
        String tMethod=args[3];
        boolean methodFound=false;

        
        List<String> procDir = new ArrayList<String>();
        procDir.add(targetDirectory);

        // Set Soot options
        soot.G.reset();
        Options.v().set_process_dir(procDir);
        // Options.v().set_prepend_classpath(true);
        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().set_keep_line_number(true);
        Options.v().setPhaseOption("cg.spark", "verbose:false");

        Scene.v().loadNecessaryClasses();

        SootClass entryClass = Scene.v().getSootClassUnsafe(mClass);
        SootMethod entryMethod = entryClass.getMethodByNameUnsafe("main");
        SootClass targetClass = Scene.v().getSootClassUnsafe(tClass);
        SootMethod targetMethod = entryClass.getMethodByNameUnsafe(tMethod);
        

        Options.v().set_main_class(mClass);
        Scene.v().setEntryPoints(Collections.singletonList(entryMethod));

        // System.out.println (entryClass.getName());
        System.out.println("tclass: " + targetClass);
        System.out.println("tmethod: " + targetMethod);
        System.out.println("tmethodname: " + tMethod);
        Iterator mi = targetClass.getMethods().iterator();
        while (mi.hasNext()) {
            SootMethod sm = (SootMethod)mi.next();
            // System.out.println("method: " + sm);
            if(sm.getName().equals(tMethod))
            {methodFound=true; break;}
        }

        if(methodFound) {
            printInfo(targetMethod);

	    /*****************************************************************
             * XXX The function doAnalysis is the entry point for the Kildall's 
             * fix-point algorithm over the LatticeElement.
             ******************************************************************/
            func_List.add(targetMethod);
            getJimpleBody();
            if (!targetMethod.isPhantom() && targetMethod.isConcrete())
            {
                Body body = targetMethod.retrieveActiveBody();

                int lineno = 0;
               
                for (Unit u : body.getUnits()) {
                    if (!(u instanceof Stmt)) {
                        continue;
                    }
                   
                    Analysis.ir_lines.put(u,lineno);
                    lineno++;
                }
                System.out.println(body);
            
                // Kildalls(body,targetDirectory,tMethod, tClass);
            }
            My_lattice_element.call_func_st.push(targetMethod);
            My_lattice_element.static_class = entryClass;
            Kildalls(targetMethod);
            String file_output_path = targetDirectory+"/"+tMethod+"_output.txt";
            BufferedWriter writer = null;
            try{
                writer = new BufferedWriter(new FileWriter(file_output_path));
                // true parameter in FileWriter constructor enables append mode
                print_progP(tMethod,tClass,writer);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }finally {
                try {
                    // Close the BufferedWriter (if it has been initialized)
                    if (writer != null) {
                        writer.close();
                        System.out.println("File "+file_output_path+" closed successfully.");
                    }
                } catch (IOException e) {
                    System.err.println("Error closing the file "+file_output_path+": " + e.getMessage());
                }
            }

	    for (SootMethod method : targetClass.getMethods()){
		drawMethodDependenceGraph(targetMethod);
	    }

	    doAnalysis(targetMethod, targetClass.getMethods());
        } else {
            System.out.println("Method not found: " + tMethod);
        }
    }
    


    private static void drawMethodDependenceGraph(SootMethod entryMethod){
        if (!entryMethod.isPhantom() && entryMethod.isConcrete())
        {
            Body body = entryMethod.retrieveActiveBody();
            ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);
            //ExceptionalBlockGraph  graph = new ExceptionalBlockGraph (body);

            CFGToDotGraph cfgForMethod = new CFGToDotGraph();
            cfgForMethod.drawCFG(graph);
            DotGraph cfgDot =  cfgForMethod.drawCFG(graph);
            cfgDot.plot("cfg.dot");
        }
    }

    public static void printUnit(int lineno, Body b, Unit u) {
        UnitPrinter up = new NormalUnitPrinter(b);
        u.toString(up);
        String linenostr = String.format("%02d", lineno) + ": ";
        System.out.println(linenostr + up.toString());
    }

    private static void printInfo(SootMethod entryMethod) {
        if (!entryMethod.isPhantom() && entryMethod.isConcrete()) {
            Body body = entryMethod.retrieveActiveBody();

            int lineno = 0;
            for (Unit u : body.getUnits()) {
                if (!(u instanceof Stmt)) {
                    continue;
                }
                Stmt s = (Stmt) u;
                printUnit(lineno, body, u);
                lineno++;
            }

        }
    }

    /**
     * @param lines
     * @param file
     */
    public static void dataSave(List<String> lines, String file) {

    File fout = new File(file);

    try (FileWriter fileWriter = new FileWriter(fout);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

        for(String str: lines)
        {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
        }
    } catch (FileNotFoundException e) {
        System.out.println("Unable to open file, file not found.");
    } catch (IOException e) {
        System.out.println("Unable to write to file." + fout.getName());
    }
    }
}  




