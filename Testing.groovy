@Grab('org.xerial:sqlite-jdbc:3.50.3.0')
@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql
import groovy.sql.GroovyRowResult
import groovy.transform.Canonical
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JOptionPane

class Testing {
  static Sql conn
  
  static void main(args) {
    init('/home/jbird/IdeaProjects/TomeOfHours/library2.sqlite3')

    if(args.size() > 0) {
      args.each { arg ->
        String name,description
        Aspect a

        switch(arg) {
          case "make-aspect":
            (name,description) = splitPaste(getInput())
            try {
              Aspect.getAspect(name)
              println "Aspect '$name' already exists."
            } catch(ignored) {
              a = Aspect.makeAspect(name,description)
              println a
            }
            break
          default:
            println "Command '$arg' not implemented."
        }
      }
    }
  }
  
  static void init(String path) {
    conn = Sql.newInstance("jdbc:sqlite:$path")
    
    fillBasics()
  }
  static void fillBasics() {
    try{
      Aspect.getAspect('Forge')
    } catch(ignored) {
      def a = Aspect.makeAspect('Forge',"'Fire', I once read, 'is the winter that warms and the spring that consumes.' [The principle of the Forge transforms and destroys.]")
      Principle.makePrinciple(a,'FF8E3F')
    }
    try{
      Aspect.getAspect('Grail')
    } catch(ignored) {
      def a = Aspect.makeAspect('Grail',"Hunger, lust, the drowning waters. [The principle of the Grail honours both the birth and the feast.]")
      Principle.makePrinciple(a,'FF614F')
    }
    try{
      Aspect.getAspect('Knock')
    } catch(ignored) {
      def a = Aspect.makeAspect('Knock',"The principle called Knock permits no seal and no isolation. It thrusts us gleefully out of the safety of ignorance. [Knock opens doors and unseams barriers.]")
      Principle.makePrinciple(a,'B54EFC')
    }
    try{
      Aspect.getAspect('Sky')
    } catch(ignored) {
      def a = Aspect.makeAspect('Sky',"Wind, storm, echo, song; the intricacies of mathematics and the principles of flight. Law's touch is lighter than we sometimes think. [Matters of balance, harmony and necessity.]")
      Principle.makePrinciple(a,'2C69E1')
    }
    try{
      Aspect.getAspect('Edge')
    } catch(ignored) {
      def a = Aspect.makeAspect('Edge',"All conquest occurs at the Edge. One who dwells there is blind, and cannot be wounded. Another is strong, and grows stronger. [Edge is the principle of battle and of struggle.]")
      Principle.makePrinciple(a,'D6DE4A')
    }
    try{
      Aspect.getAspect('Scale')
    } catch(ignored) {
      def a = Aspect.makeAspect('Scale',"Hard without, hard within, hard to rouse, harder to subdue. [What is left of the crude powers of the deep earth.]")
      Principle.makePrinciple(a,'CB9F4D')
    }
    try{
      Aspect.getAspect('Rose')
    } catch(ignored) {
      def a = Aspect.makeAspect('Rose',"'The rose which encompasseth all'. Nine directions to new horizons. [Exploration? Enlightenment? Hope?]")
      Principle.makePrinciple(a,'EF63FF')
    }
    try{
      Aspect.getAspect('Heart')
    } catch(ignored) {
      def a = Aspect.makeAspect('Heart',"The Heart Relentless beats to protect the skin of the world we understand. [The Heart is the principle that continues and preserves.]")
      Principle.makePrinciple(a,'FF7F8C')
    }
    try{
      Aspect.getAspect('Lantern')
    } catch(ignored) {
      def a = Aspect.makeAspect('Lantern',"'Life is a pure flame, and we live by an invisible Sun within us.' - Thomas Browne. [Lantern is the principle of the secret place sometimes called the House of the Sun, and of the light above it.]")
      Principle.makePrinciple(a,'FFE300')
    }
    try{
      Aspect.getAspect('Moon')
    } catch(ignored) {
      def a = Aspect.makeAspect('Moon',"Secrets are soft; night is softer still; the sea speaks. It is not always wise to listen. [The nocturnal, the forgotten.]")
      Principle.makePrinciple(a,'CBBCD6')
    }
    try{
      Aspect.getAspect('Moth')
    } catch(ignored) {
      def a = Aspect.makeAspect('Moth',"I knew a man who captured moths in a bell-jar. On nights like this, he would release them one by one to die in the candle. [Moth is the wild and perilous principle of chaos and yearning.]")
      Principle.makePrinciple(a,'F1E9C2')
    }
    try{
      Aspect.getAspect('Nectar')
    } catch(ignored) {
      def a = Aspect.makeAspect('Nectar',"The green wealth in the world's veins; the pulse of the seasons. [Long ago, some called this principle Blood.]")
      Principle.makePrinciple(a,'2CD391')
    }
    try{
      Aspect.getAspect('Winter')
    } catch(ignored) {
      def a = Aspect.makeAspect('Winter',"... [Winter is the principle of silence, of endings, and of those things that are not quite dead.]")
      Principle.makePrinciple(a,'BDEFFF')
    }
    try{
      Aspect.getAspect('Memory')
    } catch(ignored) {
      Aspect.makeAspect('Memory',"Something remembered might be something understood. [Memories can be used to help with crafting and to upgrade Skills. Memories disappear each dawn - except Persistent ones.]")
    }
  }
  
  static String getInput(component=null) {
    def area = new JTextArea(5,20)
    int opt = JOptionPane.showConfirmDialog(component,area,"Enter the text",JOptionPane.OK_CANCEL_OPTION)
    if(opt == JOptionPane.OK_OPTION) {
      return area.text
    } else {
      return ''
    }
  }
  static List<String> splitPaste(String input) {
    input.split('[\r\n]+')*.trim().findAll { it }
  }
  
  @Canonical
  static class Aspect {
    int ID
    String name
    String description
    String notes
    
    Aspect(){}
    Aspect(GroovyRowResult row) {
      ID = row.AspectID
      name = row.Name
      description = row.Description
      notes = row.Notes
    }
    
    static Aspect makeAspect(String name,String description) {
      def a = new Aspect()
      a.name = name
      a.description = description
      def keys = conn.executeInsert('''
        INSERT INTO Aspects
          (Name,Description)
        VALUES
          (?,?)''',[name,description])
      a.ID = keys[0][0]
      
      return a
    }
    
    static Aspect getAspect(String name) {
      def row = conn.firstRow('''SELECT * FROM Aspects WHERE Name=?''',name)
      return new Aspect(row)
    }
  }
  @Canonical
  static class Principle {
    int ID
    int aspectID
    String color
    
    Principle(){}
    Principle(GroovyRowResult row) {
      ID = row.PrincipleID
      aspectID = row.AspectID
      color = row.Color
    }
    static Principle makePrinciple(Aspect aspect,String color) {
      def p = new Principle()
      p.aspectID = aspect.ID
      p.color = color
      def keys = conn.executeInsert('''
        INSERT INTO Principles
          (AspectID,Color)
        VALUES
          (?,?)''',[aspect.ID,Integer.decode("0x$color")])
      p.ID = keys[0][0]
      
      return p
    }
    static Principle getPrinciple(String name) {
      def a = Aspect.getAspect(name)
      def row = conn.firstRow('SELECT * FROM Principles WHERE AspectID=?',a.ID)
      return new Principle(row)
    }
  }
}
