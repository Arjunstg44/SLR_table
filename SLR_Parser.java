import java.util.*;
class SLR
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter all the Terminals,Comma separated(NO spaces)(EPSILON as : eps // as you see)");        
        String term[]=sc.nextLine().split(",");
        Grammar g = new Grammar();
        g.addTerminal(term);
        System.out.println("Enter all the Non Terminals,Comma separated(NO spaces)(First One is taken as Start Symbol)");        
        String nterm[]=sc.nextLine().split(",");
        g.addNonTerminal(nterm);
        System.out.println("Enter Rules(press -1 to break)(L->R)(enter . to separate 2 symbols)(enter with|. rule numbers will then be assigned)");
        String addi=g.NonTerminal.get(0).name;//g.rules.get(0).left.name;
        g.NonTerminal.add(new Symbol("AugStrt",false));
        //g.addNonTerminal("AugStrt");
        g.addProduction("AugStrt->"+addi+"|"); 
        while(true)
        {
            String r=sc.nextLine();
            //System.out.println("-"+r+"-");
            if(r.equals("-1"))
                break;
            g.addProduction(r+"|");
        }
        //AUGMENTED GRAMMAR FORMATION:
      /*  String addi=g.NonTerminal.get(0).name;//g.rules.get(0).left.name;
        g.NonTerminal.add(new Symbol("AugStrt",false));
        //g.addNonTerminal("AugStrt");
        g.addProduction("AugStrt->"+addi+"|");*/    
        g.generate_numbered_production();
        g.display();
        sc.close();
        //Generate FA
        //generate_FA()
        //
    }
}

class Grammar
{
    ArrayList<Symbol> Terminal;
    ArrayList<Symbol> NonTerminal;
    Symbol start;
    ArrayList<Production> rules;
    ArrayList<Numbered_Production> numbered_rules;
    //HashMap<Symbol,>
    Grammar()
    {
        Terminal = new ArrayList<Symbol>();
        NonTerminal = new ArrayList<Symbol>();
        rules=new ArrayList<Production>();
        numbered_rules=new ArrayList<Numbered_Production>();
    }
    //different rules than easier , 2 non Terminals never occur together
    //public HashMap<Symbol,HashSet<Symbol>> getFollow()
    //{}
    //see if 2 sets are same
    boolean equality(HashSet<Symbol> s1,HashSet<Symbol> s2)
    {
        System.out.println(s1.size()+"|"+s2.size());
        //boolean result=true;
        try{
            if(s1.size()!=s2.size())
                return false;
        
        else
        {
            for(Symbol iter :s1)
            {
                s2.remove(iter);
            }
            if(s2.size()==0)
                return true;
            
        }}
        finally{;}
        return false;
    }
    public ArrayList<ArrayList<Symbol>> getRules(Symbol t)
    {
        for(Production p : rules)
        {
            if(p.left.name==t.name)
            {
                //System.out.print("getRules match:"+t.name);
                return p.right;
            }
        }
        return new ArrayList<ArrayList<Symbol>>();
    }
    public void addTerminal(String arr[])
    {
        for(String i:arr)
        {
            Symbol t = new Symbol(i,true);
            Terminal.add(t);
        }
    }
    public void addNonTerminal(String arr[])
    {
        for(String i:arr)
        {
            Symbol t = new Symbol(i,false);
            NonTerminal.add(t);
        }
    }
    public void addProduction(String s)
    {
        String lr[] = s.split("->");
        Production p = new Production();
        int lindex=myIndex(NonTerminal,lr[0]);
        //System.out.print(lr[0]);
        //System.out.print("lr[1]:"+lr[1]);
        if(lindex==-1)
        {System.out.println("Not valid Terminal.not Added in Production list");return;}
        else {
            boolean flag=false;
            for(Production x : rules)
            {
                if((x.left).equals(NonTerminal.get(lindex)))
                {p=x;flag=true;break;}
            }
            if(flag==false){
                //p=new Production();
                p.left=NonTerminal.get(lindex);
            }
        }
        String ors[]=lr[1].split("\\|");
        //System.out.print("ors[0]:"+ors[0]);
        for(String subrule : ors)
        {
            ArrayList<Symbol> alpha = new ArrayList<Symbol>();
            String[] syms= subrule.split("\\.");
           // System.out.print("-syms[0]"+syms[0]);
            for(String sym : syms)
            {
                //System.out.print(sym+"*");
                int rindex=myIndex(Terminal,sym);
                if(rindex==-1)
                {
                    rindex=myIndex(NonTerminal,sym);
                
                    if(rindex==-1)
                    {
                        System.out.println("Not valud symbol . not changed");break;
                    }
                    
                    else{
                        //System.out.println("Getting added:"+NonTerminal.get(rindex).name);
                        alpha.add(NonTerminal.get(rindex));
                    }
                }
                else{
                    //System.out.println("Getting added:"+Terminal.get(rindex).name);
                    alpha.add(Terminal.get(rindex));
                }
            }
            p.right.add(alpha);
        }
        rules.add(p);
    }
    public int myIndex(ArrayList<Symbol> list, String sym)
    {
        int idx=-1,ctr=-1;
        for(Symbol elem : list)
        {
            ctr++;
            if(sym.equals(elem.name))
            {
                idx=ctr;break;
            }
        }
        return idx;
    }
    public void generate_numbered_production()
    {
        int ctr=1;
        for(ArrayList<Symbol> rr:rules.get(0).right)
        {
            Numbered_Production p = new Numbered_Production(ctr++,rules.get(0).left,rr);
            numbered_rules.add(p);
        }
        for(int i=0;i< rules.size();i++)
        {
            if(i>0)
            {
                if(rules.get(i-1).left.name==rules.get(i).left.name)
                {
                    ;
                }
                else
                {    for(ArrayList<Symbol> r : rules.get(i).right)
                    {
                        
                        Numbered_Production p = new Numbered_Production(ctr++,rules.get(i).left,r);
                        numbered_rules.add(p);
                    }
                    //numbered_rules.add(p);
                }       
            }
        }
    }
    boolean all_items_equal(ArrayList<items> a , ArrayList<items> b)
    {
        int ctr=0;
        boolean result = true,flag;
        for(items i: a)
        {
            flag=false;
            for(items j: b)
            {
                if(i.equals(j))
                    flag=true;
            }
            if(flag==true)
            ctr++;
        }
        
        if(ctr==a.size() && a.size() == b.size())
         return result;
        return false;
    }
    public void display()
    {
        System.out.println("Printing Terminals");
        for(Symbol s:Terminal)
        {
            System.out.print(s.name+"_");
        }
        System.out.println("\nPrinting Terminals");
        for(Symbol s:NonTerminal)
        {
            System.out.print(s.name+"_");
        }
        System.out.println("\nPrinting Production Rules");
        for(Production p:rules)
        {
            System.out.print("\n"+p.left.name+"->");
            for(ArrayList<Symbol> i : p.right)
            {
                //System.out.print(i.size());
                for(Symbol sk :i)
                {
                    System.out.print(sk.name+"");
                }
                System.out.print("|");
            }
        }
        System.out.println("\n_______________\nNUMBERED PRODUCTIONS::___");
        for(Numbered_Production p : numbered_rules)
        {
            System.out.println(p.number+" "+STR(p.right,p.left));
        }
        ArrayList<ArrayList<items>> itemset= new ArrayList<ArrayList<items>>()  ;
        ArrayList<items> i0= new ArrayList<items>();//a single Item containing an array of 2 element tuples
        items ii= new items(1,0);
        //System.out.print("CHECKING 1,1");print_item(closure(new items(1,1)));
        //i0.add(ii);//now add closure
        i0=closure(ii);
        print_item(i0);
        itemset.add(i0);
        Stack<ArrayList<items>> rec = new Stack<ArrayList<items>>();
        rec.push(i0);
        int myctr=0;
        while(!rec.isEmpty())
        {
           // if(myctr++>20)
             //   break;
            ArrayList<items> kk=rec.pop();
            HashSet<items> abcd= new HashSet<items>(kk);
            kk=(ArrayList<items>)(kk);
            //itemset.add(kk);
            HashMap<Symbol,ArrayList<items>> hs = generate_next(kk);
            //System.out.print("hashmap suze"+hs.size());
            //if hashmap empty, break
            //if(hs.size()==0)
              //  break;
            System.out.println("\n__________________________________________________");
            for(Symbol bomb :hs.keySet())
            {

                //print_item(aol);
                ArrayList<items> aol = hs.get(bomb);
                if(aol.size()==0)
                    continue;
                System.out.print(bomb.name+" : ");
                print_item(aol);
                //ArrayList<items> aol = (ArrayList<items>)(aol2);
                //if aol is not present in itemset already, add it to the itemset, else link it to respective state in itemset
                //override Equals in items Class and run loop(slow)
                ArrayList<ArrayList<items>> temp = new ArrayList<ArrayList<items>>();
                boolean doesnt=true;
                for(ArrayList<items> ccc : itemset)
                {
                    //print_item(ccc);
                    //print_item(aol);
                    //System.out.print(all_items_equal(ccc, aol));

                    if(all_items_equal(ccc, aol)==false)
                    {
                        //print_item(ccc);
                        //print_item(aol);
                        
                        //System.out.print(all_items_equal(ccc, aol));
                        //.add(aol);
                        //if non terminating, add to stack
                        boolean terminates= true;
                        for(items last : ccc)
                        {
                           if(numbered_rules.get(last.rule_number-1).right.size()==last.loc_index)
                           ;
                           else
                            terminates=false; 
                        }
                        if(terminates==false)
                        {
                            //rec.push(aol);
                            //System.out.print(bomb.name+" : ");
                            
                            //print_item(aol);
                        }
                        else
                        {
                            //temp.add(aol);
                        }
                        
                    }
                    else{doesnt=false;}
                    //else link them<ol item arraylist with new one ka for print>
                    //itemset.add()

                }
                if(doesnt == true)
                {
                    itemset.add(aol);rec.push(aol);
                }
                else{System.out.println("<Duplicate, has previously occured>");}
                //itemset.addAll(temp);

            }
        }
        /*
        System.out.print("\n<PRINTING Table>\n");
        System.out.println("+   *   (   )   id  E   T   F");
        System.out.println("   _   4   _   _   1   2   3");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        System.out.println("_   _   _   _   _   _   _   _");
        */
        
        for(ArrayList<items> ioi : itemset)
        {
            print_item(ioi);
        }
    }
    HashMap<Symbol,ArrayList<items>> generate_next(ArrayList<items> state)
    {
        System.out.print("Inside generate_next");print_item(state);
        HashMap<Symbol,ArrayList<items>> delta = new HashMap<Symbol,ArrayList<items>>();
        //closure needed to be calculated only if we face a nonTerminal(operator grammar: nonterminal++ will yield terminal)
        for(Symbol s: NonTerminal)
        {
            ArrayList<items> state2 = new ArrayList<items>();
            for(int i = 0; i<state.size();i++)
            {   
                items all=state.get(i);
                //symbols followd by the dot
                if(numbered_rules.get(all.rule_number-1).right.size()<=all.loc_index)
                {
                continue;
                }    
                Symbol got=numbered_rules.get(all.rule_number-1).right.get(all.loc_index);
                if(got.name.equals(s.name))//only modify the item set . change hashmap outside items loop
                {
                    //System.out.print(s.name);
                    //all.loc_index++;
                    items ali = new items(all.rule_number,all.loc_index+1);
                    state2.add(ali);
                    state2.addAll(closure(ali));
                    ArrayList<items> newlist= new ArrayList<items>();
                    for(items ios : state2)
                    {
                        if(!newlist.contains(ios))
                        newlist.add(ios);
                    }
                    state2.clear();state2.addAll(newlist);

                }
            }
            delta.put(s,state2);
        }
        for(Symbol s: Terminal)
        {
            ArrayList<items> state2 = new ArrayList<items>();
            for(int i = 0; i<state.size();i++)
            {   
                items all=state.get(i);
                //symbols followd by the dot
                if(numbered_rules.get(all.rule_number-1).right.size()<=all.loc_index)
                {
                continue;
                }
                Symbol got=numbered_rules.get(all.rule_number-1).right.get(all.loc_index);
                if(got.name.equals(s.name))//only modify the item set . change hashmap outside items loop
                {
                    //System.out.print(s.name);
                    //all.loc_index++;
                    items ali = new items(all.rule_number,all.loc_index+1);
                    state2.add(ali);
                    state2.addAll(closure(ali));
                    ArrayList<items> newlist= new ArrayList<items>();
                    for(items ios : state2)
                    {
                        if(!newlist.contains(ios))
                        newlist.add(ios);
                    }
                    state2.clear();state2.addAll(newlist);
                }
            }
            delta.put(s,state2);
        }
        return delta;
    }
    ArrayList<items> closure(items i)
    {
        ArrayList<items> i0= new ArrayList<items>();
        //i0.add(i);
        Stack<items> rec = new Stack<items>();
        rec.push(i);
        while(!rec.isEmpty())
        {
            
            items k = rec.pop();
            i0.add(k);
            if(numbered_rules.get(k.rule_number-1).right.size()<=k.loc_index)
            {
                continue;
            }
            Symbol s = numbered_rules.get(k.rule_number-1).right.get(k.loc_index);
            boolean flag = true;
            if(!s.is_term)
            {
                //if it is a non-terminal, it must have a production
                for(Numbered_Production pp : numbered_rules)
                {
                    if((pp.left.name).equals(s.name))
                    {
                        //System.out.print(STR(pp.right,pp.left));
                        for(items nested: i0)
                        {
                            if(nested.rule_number==pp.number)
                                flag=false;
                        }
                        if(flag)
                        {rec.push(new items(pp.number,0));//System.out.print(STR(pp.right,pp.left));
                        }
                    }
                }
            }
        }

        return i0;
    }
    void print_item(ArrayList<items> it)
    {
       // ArrayList<items> newlist= new ArrayList<items>();
        /*
        for(items i : it)
        {
            if(!newlist.contains(i))
                newlist.add(i);
        }*/
        System.out.print("\n{");
        for(items i:it)
        {
            Numbered_Production p=numbered_rules.get(i.rule_number-1);
            System.out.print("_("+p.left.name+">");//+")_;");
            int j;
            for( j=0;j< p.right.size();j++)
            {
                if(j==i.loc_index){System.out.print(".");}
                System.out.print(p.right.get(j).name);
            }
            if(j==i.loc_index){System.out.print(".");}
            System.out.print(")_");
        }
        System.out.println("}");
    }
    static String STR(ArrayList<Symbol> d,Symbol left)
    {
        String result=left.name+">";
        for(Symbol i : d)
        {
            result+=i.name;
        }
        return result;
    }
    //generate_FA()
    //{}
}
class items
{    
    int rule_number;
    int loc_index;
    boolean terminated;
    items(int a,int b)
    {
        rule_number=a;
        loc_index=b;
        terminated=false;
    }
    //generate itemset from closure::::
    //equality
    @Override
    public boolean equals(Object o)
    {
        items oo = (items)o;
        if(this.loc_index==oo.loc_index && this.rule_number==oo.rule_number)
            return true;
        return false;
    }
    @Override
    public int hashCode()
    {
        String s = this.rule_number+"_"+this.loc_index ;
        return s.hashCode();
    }
}

class Production
{
    Symbol left;
    ArrayList<ArrayList<Symbol>> right;
    Production()
    {
        left=null;
        right=new ArrayList<ArrayList<Symbol>>();
    }
    //add necessary for to be a part of itemset
}
class Symbol
{
    final String name;
    final boolean is_term;
    Symbol(String s,boolean val)
    {
        name=s;
        is_term=val;
    }
    @Override
    public boolean equals(Object o)
    {
        Symbol oo = (Symbol)o;
        if(this.name==oo.name && this.is_term==oo.is_term)
        {
            return true;
        }
        return false;
    }
    @Override    
    public int hashCode()
    {
        return this.name.hashCode();
    }

}
class Numbered_Production
{
    int number;
    Symbol left;
    ArrayList<Symbol> right;
    Numbered_Production(int n,Symbol s , ArrayList<Symbol> rights)
    {
        number=n;
        left=s;
        right=rights;
    }
}