

import java.util.*;

import java_cup.lalr_state;
import soot.jimple.Stmt;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.JastAddJ.NEExpr;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NeExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.IdentityStmt;


public class My_lattice_element implements LatticeElement {

   // HashMap<String, Set<String>> lat_ele = new HashMap<String, Set<String>>();
    LatticeElement le2;
    int prgPoint;
    
    public static HashMap<SootMethod, HashMap<String, Unit>> Name2CFG = new LinkedHashMap<>();
    public static HashMap<String, SootMethod> Call2CFG = new HashMap<>();
    public HashMap<String,HashMap<String,Set<String>>> lat_ele = new HashMap<>();
    public static Stack< HashMap<String, Set<String>> > actual_para_st = new Stack<>();
    public static Stack<SootMethod> call_func_st = new Stack();
    public static HashMap<SootMethod,List<Map.Entry<LatticeElement, Boolean>> > call_func_prog_pt = new HashMap();
    public static SootClass static_class;
    My_lattice_element() {
    } // >>>>>>>>>>>>>>>>>>>>>>>Default constructor

    My_lattice_element(LatticeElement le) {// >>>>>>>>parameterized constructor
        this.le2 = le;
    }

    // ***************************************************************************************************************
    // */

    @Override
       /* ---------- JOIN ----------*/
     

    public LatticeElement join_op(LatticeElement r){
        My_lattice_element le = new My_lattice_element();


        My_lattice_element le1 = (My_lattice_element)this; 
        My_lattice_element le2 = (My_lattice_element)r;

        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : le1.lat_ele.entrySet()) {
            HashMap<String,Set<String>> set1 = new HashMap();
            for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                Set<String> set2 = new HashSet<>();
                set2.addAll(e2.getValue());
                
                set1.put(e2.getKey(),
                            set2);
            }
            le.lat_ele.put(e.getKey(),set1);
        }
   
        
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : le2.lat_ele.entrySet()) {
            HashMap<String,Set<String>> set1 = new HashMap();
            if(le.lat_ele.containsKey(e.getKey()) ){
              
                for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                    Set<String> set2 = new HashSet<>();
                    
                    if (le.lat_ele.get(e.getKey()).containsKey(e2.getKey())){
                        set2.addAll(le.lat_ele.get(e.getKey()).get(e2.getKey()));
                        set2.addAll(e2.getValue());
                       
                        set1.put(e2.getKey(),set2);
                    }
                    else{
                        set2.addAll(e2.getValue());
                       
                        set1.put(e2.getKey(),
                                set2);
                    }
                }
                
            }
                        else{
                
                for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                    Set<String> set2 = new HashSet<>();
                    set2.addAll(e2.getValue());
                    
                    set1.put(e2.getKey(),
                                set2);
                }
            }
            }
       
        return (LatticeElement)le;
    }


      /* ---------- EQUALS ----------*/
      public boolean equals(LatticeElement r){
       
        My_lattice_element le1 = (My_lattice_element)r;
        My_lattice_element le2 = (My_lattice_element)this;
        
        if (le1.lat_ele.size() != le2.lat_ele.size()){
            return false;
        }
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e: le1.lat_ele.entrySet()){
            if(le2.lat_ele.containsKey(e.getKey())){
                for (HashMap.Entry<String,Set<String>> e1 : e.getValue().entrySet()) {
                   
                    if(le2.lat_ele.get(e.getKey()).containsKey(e1.getKey()) == false){
                        return false;
                    }
                    
                    if(le2.lat_ele.get(e.getKey()).get(e1.getKey()).equals(le1.lat_ele.get(e.getKey()).get(e1.getKey())) == false){
                        return false;
                    }
                }
            }
            else{
                return false;
            }
        }
        return true;
    }

    public LatticeElement tf_idpara(Stmt st){
        My_lattice_element le = new My_lattice_element();
        My_lattice_element le1 = (My_lattice_element)this;
        
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : le1.lat_ele.entrySet()) {
            HashMap<String,Set<String>> set1 = new HashMap();
            for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                Set<String> set2 = new HashSet<>();
                set2.addAll(e2.getValue());
                set1.put(e2.getKey(), set2);
            }
            le.lat_ele.put(e.getKey(),set1);
        }
       
            if(!actual_para_st.empty()){
                HashMap<String, Set<String>> fun_pop = actual_para_st.pop();
                Iterator<String> iterator = fun_pop.get("callsite").iterator();
                String callString = iterator.next();
                if(!actual_para_st.empty()){
                    iterator = actual_para_st.peek().get("callsite").iterator();
                    callString = iterator.next()+","+callString;
                }
                //System.out.println(" ---- callString:"+callString);
                actual_para_st.push(fun_pop);
                IdentityStmt idStmt = (IdentityStmt) st;
                HashMap<String,Set<String>> le3 = le.lat_ele.get(callString);
                for (Map.Entry<String, Set<String>> e1 : fun_pop.entrySet()) {
                    String k = e1.getKey();
                    Set<String> values = e1.getValue();
                    System.out.println(" ---- key:"+(k)+" values:"+values);
                    if(k.split("\\.").length == 3 || k.equals(((IdentityStmt)st).getLeftOp().toString()) ){
                        Set<String> set3 = new HashSet<>();
                        set3.addAll(values);
                        le3.put(k,set3);
                    }
                }
               
            }
            return (LatticeElement)le;
        }

    /***************************************************************************************************** */

    @Override
  
          /* ---------- ASSIGNMENT ----------*/
      
    public LatticeElement tf_assignstmt(Stmt st){

        My_lattice_element le = new My_lattice_element();
       
        My_lattice_element this_tc = (My_lattice_element)this;
       
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : this_tc.lat_ele.entrySet()) {
            HashMap<String,Set<String>> set1 = new HashMap();
            for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                Set<String> set2 = new HashSet<>();
                set2.addAll(e2.getValue());
               
                set1.put(e2.getKey(),
                            set2);
            }
            le.lat_ele.put(e.getKey(),set1);
        }
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : this_tc.lat_ele.entrySet()) {
           
            HashMap<String,Set<String>> le4 = new HashMap();
            for (HashMap.Entry<String,Set<String>> ele1 : le.lat_ele.get(e.getKey()).entrySet()) {
                Set<String> set3 = new HashSet<>();
                set3.addAll(ele1.getValue());
                le4.put(ele1.getKey(),set3);
            }

            if (st.containsInvokeExpr() && st instanceof AssignStmt) {
                AssignStmt as = (AssignStmt)st;
                InvokeExpr ie = st.getInvokeExpr();
                
                if (ie instanceof StaticInvokeExpr) {
                    StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) ie;

                   
                    SootMethod method = staticInvokeExpr.getMethod();
                    Set<String> set2 = new HashSet<>();
                    HashMap<String, Unit> ir_line = My_lattice_element.Name2CFG.get(call_func_st.peek());
                    String call_string="";
                    HashMap<String, Set<String>> actual_para_map = new HashMap();
                    List<Map.Entry<LatticeElement, Boolean>> actual_para_list = new ArrayList<>();

                    

                    for(HashMap.Entry<String, Unit> ir_line_no : ir_line.entrySet()){
                        if(((Stmt)ir_line_no.getValue()).equals(st)){
                            call_string = call_func_st.peek().getName().toString()+".in"+ir_line_no.getKey();
                        }                        
                    }
                    if(call_string.length()==0)
                        break;
                    Set<String> set3 = new HashSet<>();
                    set3.add(call_string);
                    actual_para_map.put("callsite",set3);                        

                    if(!actual_para_st.empty()){
                        HashMap<String,Set<String>> last_call = actual_para_st.peek();
                        System.out.println(last_call.get("callsite").iterator().next().split("\\.")[0]);
                        SootMethod local_Method = static_class.getMethodByNameUnsafe(last_call.get("callsite").iterator().next().split("\\.")[0]);
                        call_string = ir_line.get("callsite")+","+call_string;
                    }
                    List<Map.Entry<LatticeElement, Boolean>> pt = call_func_prog_pt.get(method);
                    Map.Entry<LatticeElement, Boolean> e2 = new AbstractMap.SimpleEntry<>(pt.get(0).getKey(), true);
                   
                    Analysis ane=new Analysis();
                    if( !(((My_lattice_element)pt.get(0).getKey()).lat_ele.containsKey(call_string)) ){
                       
                       
                        if(call_string.length()!=0)
                        ((My_lattice_element)e2.getKey()).lat_ele.put(call_string,new HashMap<>() );
                        pt.set(0,e2);
                    }
                    if (!method.isPhantom() && method.isConcrete())
                    {
                        Body body = method.retrieveActiveBody();
                        //local
 
                            
                            List<Local> ac_para = body.getParameterLocals();
                            System.out.println(method.toString());
                            System.out.println(ac_para);
 
                            List<Value> frml_para = staticInvokeExpr.getArgs();
                            
                            int no_frml_para= 0;
                            for (int i = 0; i < frml_para.size(); i++) {
                                Set<String> set4 = new HashSet<>();
                                System.out.println(e.getValue());
                                if(!e.getValue().containsKey(frml_para.get(i)))
                                    break;
                                System.out.println(e.getValue().get(frml_para.get(i).toString()));
                                set4.addAll(e.getValue().get(frml_para.get(i).toString()));
                            
                                actual_para_map.put(ac_para.get(i).toString(),set4);
                                no_frml_para++;
                            }
                            if(!(no_frml_para== frml_para.size()))
                                continue;
                            for (HashMap.Entry<String,Set<String>> mp : e.getValue().entrySet()) {
                                if(mp.getKey().split("\\.").length==3){
                                    Set<String> set4= new HashSet<>();
                                    set2.addAll(mp.getValue());
                                    actual_para_map.put(e.getKey(),set4);
                                }
                            }
                        }
                        actual_para_st.push(actual_para_map);
                        call_func_st.push(method);
                        le4.put(as.getLeftOp().toString(), Analysis.Kildalls(method));
                }
            
            }
            else if(st instanceof AssignStmt){

                AssignStmt as = (AssignStmt)st;
                /* v = null */
                if (as.getRightOp() instanceof NullConstant && (as.getLeftOp() instanceof InstanceFieldRef)==false){
                    Set<String> set = new HashSet<>();
                    set.add(as.getRightOp().toString());
                    le4.put(as.getLeftOp().toString(),set); 
                }
                /* v.f = null */
                else if (as.getRightOp() instanceof NullConstant && (as.getLeftOp() instanceof InstanceFieldRef)==true){
                    InstanceFieldRef left = (InstanceFieldRef) as.getLeftOp();
                        String v = left.getBase().toString();
                        String f = left.getField().getName();
                        String w = as.getRightOp().toString();

            
                        if(le4.containsKey(v)){
                            for (String str : le4.get(v)){
                               
                                if(str!="null"){
                                    String vf = (str+"."+f);
                                    Set<String> set3 = new HashSet<>();
                                    
                                    if(le4.containsKey(vf)){
                                        set3.addAll(le4.get(vf));
                                        set3.add(w);
                                    }
                                    else{
                                        set3.add(w);
                                    }
                                    le4.put(vf,set3);
                                }
                            }
                        }
                }
                else if ((as.getLeftOp().getType() == as.getRightOp().getType()) && (as.getRightOp().getType() instanceof RefType)){

                    
                    String r_op_s = as.getRightOp().toString();
                    /* v = new Obj */
                    if(as.getRightOp() instanceof NewExpr){
                        String no = call_func_st.peek().getName().toString()+".new" + String.format("%02d", Analysis.ir_lines.get((Unit)as));
                        Set<String> set = new HashSet<>();
                       
                        if(le4.containsKey(as.getLeftOp())){
                            set.addAll(le4.get(as.getLeftOp()));
                            set.add(no);
                        }
                        else{
                            set.add(no);
                        }
                        le4.put(as.getLeftOp().toString(),
                                set);
                    } 
                    /* v.f = w*/
                    else if((as.getLeftOp() instanceof InstanceFieldRef) && (as.getRightOp() instanceof InstanceFieldRef)==false){
                        InstanceFieldRef left = (InstanceFieldRef)as.getLeftOp();
                        String v = left.getBase().toString();
                        String f = left.getField().getName();
                        String w = as.getRightOp().toString();

                        if(le4.containsKey(v)){
                            for (String str : le4.get(v)){
                                                                     
                                if(str!="null"){
                                    String vf = (str+"."+f);
                                    Set<String> set = new HashSet<>();
                                    
                                    if(le4.containsKey(vf)){
                                        set.addAll(le4.get(vf));
                                        set.addAll(le4.get(w));
                                    }
                                    else{
                                        set.addAll(le4.get(w));
                                    }
                                    le4.put(vf,set);
                                
                                }
                            }
                                
                        }
                    }
                    /* v = w.f */
                    else if((as.getLeftOp() instanceof InstanceFieldRef)==false && (as.getRightOp() instanceof InstanceFieldRef)){
                        InstanceFieldRef right = (InstanceFieldRef) as.getRightOp();
                        String v = as.getLeftOp().toString();
                        String w = right.getBase().toString();
                        String f = right.getField().getName();                        

        
                        if(le4.containsKey(w)){
                            Set<String> set = new HashSet<>();
                            for (String str : le4.get(w)){
                            
                                if(str!="null"){
                                    String wf = (str+"."+f);
                                  
                                    if (le4.containsKey(wf)){
                                        set.addAll(le4.get(wf));   
                                        le4.put(v,set);
                                    }
                                }
                            }
                        }
                    }
                    else{
                        /* v=w*/
                        Set<String> set = new HashSet<>();
                        if (le4.containsKey(as.getRightOp().toString())){
                            set.addAll(le4.get(as.getRightOp().toString()));
                            le4.put(as.getLeftOp().toString(),set);
                        }
                    }
                }
            }
            le.lat_ele.put(e.getKey(),le4);
        }
        return (LatticeElement)le;
    }
   

   @Override
    /* ---------- CONDITIONALS ----------*/
    public LatticeElement tf_condstmt(boolean b, Stmt st){
        My_lattice_element le = new My_lattice_element();
   
        My_lattice_element le2 = (My_lattice_element)this;
      
        for (HashMap.Entry<String,HashMap<String,Set<String>>> e : le2.lat_ele.entrySet()) {
            HashMap<String,Set<String>> set1 = new HashMap();
            for (HashMap.Entry<String,Set<String>> e2 : e.getValue().entrySet()) {
                Set<String> set2 = new HashSet<>();
                set2.addAll(e2.getValue());
                
                set1.put(e2.getKey(),
                            set2);
            }
            le.lat_ele.put(e.getKey(),set1);
        }

 
         return (LatticeElement)le;
     }   
     }