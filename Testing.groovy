@Grab('org.xerial:sqlite-jdbc:3.50.3.0')
@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql
import groovy.lang.Tuple3
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
        String card,aspect
        def primary = false
        def secondary = false

        switch(arg) {
          case "make-aspect":
            (name,description) = splitPaste(getInput())
            try {
              Aspect.getAspect(name)
              println "Aspect '$name' already exists."
            } catch(ignored) {
              def a = Aspect.makeAspect(name,description)
              println a
            }
            break
          case "make-card":
            (name,description) = splitPaste(getInput())
            try {
              Card.getCard(name)
              println "Card '$card' already exists."
            } catch(ignored) {
              def c = Card.makeCard(name,description)
              println c
            }
            break
          case "card-primary":
            primary = true
          case "card-secondary":
            secondary = true
          case "card-aspect":
            (card,aspect) = splitPaste(getInput())
            try {
              def c = Card.getCard(card)
              def a = Aspect.getAspect(aspect)
              if(primary) c.addPrimaryAspect(a)
              else if(secondary) c.addSecondaryAspect(a)
              else c.addAspect(a)
            } catch(ignored) {
              println "Card or Aspect don't exist!"
            }
            break
          case "get-card":
            card = getInput()
            try {
              def c = Card.getCard(card)
              def aspects = c.aspects
              println c
              aspects.each { a ->
                def data = ''
                if(a.v2 != 'OTHER') {
                  data = ": $a.v2"
                } else if(a.v3 > 1) {
                  data = " ($a.v3)"
                }
                println " > $a.v1 $data"
              }
            } catch(ignored) {
              println "Card '$card' doesn't exist!"
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
    makePrincipleIfNew('Forge',"'Fire', I once read, 'is the winter that warms and the spring that consumes.' [The principle of the Forge transforms and destroys.]",'FF8E3F')
    makePrincipleIfNew('Grail',"Hunger, lust, the drowning waters. [The principle of the Grail honours both the birth and the feast.]",'FF614F')
    makePrincipleIfNew('Knock',"The principle called Knock permits no seal and no isolation. It thrusts us gleefully out of the safety of ignorance. [Knock opens doors and unseams barriers.]",'B54EFC')
    makePrincipleIfNew('Sky',"Wind, storm, echo, song; the intricacies of mathematics and the principles of flight. Law's touch is lighter than we sometimes think. [Matters of balance, harmony and necessity.]",'2C69E1')
    makePrincipleIfNew('Edge',"All conquest occurs at the Edge. One who dwells there is blind, and cannot be wounded. Another is strong, and grows stronger. [Edge is the principle of battle and of struggle.]",'D6DE4A')
    makePrincipleIfNew('Scale',"Hard without, hard within, hard to rouse, harder to subdue. [What is left of the crude powers of the deep earth.]",'CB9F4D')
    makePrincipleIfNew('Rose',"'The rose which encompasseth all'. Nine directions to new horizons. [Exploration? Enlightenment? Hope?]",'EF63FF')
    makePrincipleIfNew('Heart',"The Heart Relentless beats to protect the skin of the world we understand. [The Heart is the principle that continues and preserves.]",'FF7F8C')
    makePrincipleIfNew('Lantern',"'Life is a pure flame, and we live by an invisible Sun within us.' - Thomas Browne. [Lantern is the principle of the secret place sometimes called the House of the Sun, and of the light above it.]",'FFE300')
    makePrincipleIfNew('Moon',"Secrets are soft; night is softer still; the sea speaks. It is not always wise to listen. [The nocturnal, the forgotten.]",'CBBCD6')
    makePrincipleIfNew('Moth',"I knew a man who captured moths in a bell-jar. On nights like this, he would release them one by one to die in the candle. [Moth is the wild and perilous principle of chaos and yearning.]",'F1E9C2')
    makePrincipleIfNew('Nectar',"The green wealth in the world's veins; the pulse of the seasons. [Long ago, some called this principle Blood.]",'2CD391')
    makePrincipleIfNew('Winter',"... [Winter is the principle of silence, of endings, and of those things that are not quite dead.]",'BDEFFF')
    makeAspectIfNew('Memory',"Something remembered might be something understood. [Memories can be used to help with crafting and to upgrade Skills. Memories disappear each dawn - except Persistent ones.]")
    makeAspectIfNew('Skill',"Anything can be learnt; everything can be forgotten.")
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

  static Aspect makeAspectIfNew(String name,String description) {
    try {
      return Aspect.getAspect(name)
    } catch(ignored) {
      return Aspect.makeAspect(name,description)
    }
  }
  static Principle makePrincipleIfNew(String name,String description,String color) {
    try {
      return Principle.getPrinciple(name)
    } catch(ignored) {
      def a = makeAspectIfNew(name,description)
      return Principle.makePrinciple(a,color)
    }
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
  @Canonical
  static class Card {
    int ID
    String name
    String description
    String notes

    Card(){}
    Card(GroovyRowResult row) {
      ID = row.CardID
      name = row.Name
      description = row.Description
      notes = row.Notes
    }

    void addAspect(Aspect aspect,int count) {
      addAspectInternal(aspect,count,'OTHER')
    }
    void addAspect(Aspect aspect) {
      addAspectInternal(aspect,1,'OTHER')
    }
    void addPrimaryAspect(Aspect aspect) {
      addAspectInternal(aspect,1,'PRIMARY')
    }
    void addSecondaryAspect(Aspect aspect) {
      addAspectInternal(aspect,1,'SECONDARY')
    }
    private void addAspectInternal(Aspect aspect,int count,String type) {
      conn.execute('''
        INSERT INTO Card_Aspects
          (CardID,AspectID,AspectCount,AspectType)
        VALUES
          (?,?,?,?)''',[ID,aspect.ID,count,type])
    }

    List<Tuple3<String,String,Integer>> getAspects() {
      def rows = conn.rows('''
        select A.Name,CA.AspectCount,CA.AspectType
          from Cards C JOIN Card_Aspects CA ON C.CardID=CA.CardID
          JOIN Aspects A ON CA.AspectID=A.AspectID
        WHERE C.Name=?''',name)
      return rows.collect { row ->
        new Tuple3<String,String,Integer>(row.Name,row.AspectType,row.AspectCount)
      }
    }

    static Card makeCard(String name,String description) {
      def c = new Card()
      c.name = name
      c.description = description
      def keys = conn.executeInsert('''
        INSERT INTO Cards
          (Name,Description)
        VALUES
          (?,?)''',[name,description])
      c.ID = keys[0][0]

      return c
    }

    static Card getCard(String name) {
      def row = conn.firstRow('SELECT * FROM Cards WHERE NAME=?',name)
      return new Card(row)
    }
  }
}
