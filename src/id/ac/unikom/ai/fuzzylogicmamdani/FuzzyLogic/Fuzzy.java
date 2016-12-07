/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.ac.unikom.ai.fuzzylogicmamdani.FuzzyLogic;

import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.DrasticSum;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

/**
 *
 * @author bayu
 */
public class Fuzzy {
    public String result(double tebarBenih,double lamaPanen){
        
        Engine engine = new Engine();
        engine.setName("kasuslele");

        InputVariable penebaranBenih = new InputVariable();
        penebaranBenih.setName("BENIH");
        penebaranBenih.setEnabled(true);
        penebaranBenih.setRange(0,3000);
        penebaranBenih.addTerm(new Trapezoid("SEDIKIT",500,1500));
        penebaranBenih.addTerm(new Triangle("SEDANG",1000,2000));
        penebaranBenih.addTerm(new Triangle("BANYAK",1500,3000));
        engine.addInputVariable(penebaranBenih);

        InputVariable hasilPanen = new InputVariable();
        hasilPanen.setName("PANEN");
        hasilPanen.setEnabled(true);
        hasilPanen.setRange(0,180);
        hasilPanen.addTerm(new Trapezoid("CEPAT",30,60));
        hasilPanen.addTerm(new Triangle("SEDANG",40,120));
        hasilPanen.addTerm(new Triangle("LAMA",100,180));
        engine.addInputVariable(hasilPanen);

        OutputVariable hargaLele = new OutputVariable();
        hargaLele.setName("HARGA");
        hargaLele.setEnabled(true);
        hargaLele.setRange(100000,1500000);
        hargaLele.setDefaultValue(Double.NaN);
        hargaLele.addTerm(new Trapezoid("MURAH",200000,500000));
        hargaLele.addTerm(new Triangle("SEDANG",400000,1000000));
        hargaLele.addTerm(new Triangle("MAHAL",800000,1500000));
        hargaLele.fuzzyOutput().setAccumulation(new DrasticSum());
        hargaLele.setDefuzzifier(new Centroid(1500000));
        engine.addOutputVariable(hargaLele);


        RuleBlock ruleBlock = new RuleBlock();
        
        ruleBlock.setEnabled(true);
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setActivation(new Minimum());
        
        //1
        ruleBlock.addRule(Rule.parse("if (BENIH is BANYAK) and (PANEN is CEPAT) then HARGA is MURAH", engine));
       
//2
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDANG) and (PANEN is CEPAT) then HARGA is MURAH", engine));
        //3
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDIKIT) and (PANEN is CEPAT) then HARGA is SEDANG", engine));
        //4
        ruleBlock.addRule(Rule.parse("if (BENIH is BANYAK) and (PANEN is SEDANG) then HARGA is MURAH", engine));
        //5
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDANG) and (PANEN is SEDANG) then HARGA is SEDANG", engine));
        //6
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDIKIT) and (PANEN is SEDANG) then HARGA is MAHAL", engine));
        //7
        ruleBlock.addRule(Rule.parse("if (BENIH is BANYAK) and (PANEN is LAMA) then HARGA is MAHAL", engine));
        //8
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDANG) and (PANEN is LAMA) then HARGA is MAHAL", engine));
        //9
        ruleBlock.addRule(Rule.parse("if (BENIH is SEDIKIT) and (PANEN is LAMA) then HARGA is MAHAL", engine));

        engine.addRuleBlock(ruleBlock);

        StringBuilder status = new StringBuilder();

        if (!engine.isReady(status)) {
            throw new RuntimeException("Engine not ready. "
                    + "The following errors were encountered:\n" + status.toString());
        }

        penebaranBenih.setInputValue(tebarBenih);
        hasilPanen.setInputValue(lamaPanen);
        engine.process();
        
        return "Harga Lelije ala g dlaijuag iag iag Rp"+String.valueOf(hargaLele.getOutputValue());
    }
}
