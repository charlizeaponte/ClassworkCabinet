import java.util.Scanner;
import java.util.TreeMap;

class BoundingBox {

    private static TreeMap<Integer, String> xMap = new TreeMap<Integer, String>();
    private static TreeMap<Integer, String> yMap = new TreeMap<Integer, String>();
    private static TreeMap<Integer, String> zMap = new TreeMap<Integer, String>();

    private static void insert(String name, Integer x, Integer y, Integer z){
        String replacing = xMap.put(x, name);
        yMap.put(y, name);
        zMap.put(z, name);
        if (replacing == null) {
            System.out.println("Added " + name);
        }
        else {
            System.out.println("Updated " + name);
        }
    }

    private static void delete(String name){
        boolean removed = xMap.values().remove(name);
        yMap.values().remove(name);
        zMap.values().remove(name);
        if (removed) {
            System.out.println(name + " has been removed.");
        }
        else {
            System.out.println(name + " does not exist.");
        }
    }

    private static void getBox(){
        int length = zMap.lastKey() - zMap.firstKey();
        int width = xMap.lastKey() - xMap.firstKey();
        int height = yMap.lastKey() - yMap.firstKey();
        System.out.println(length * width * height);
    }

    public static void main(String[] args){
        
        Scanner input = new Scanner(System.in);

        System.out.println("# of instructions: ");
        int n = input.nextInt();
        input.nextLine();

        while(n != 0){
            System.out.println("Provide instruction: ");
            String[] line = input.nextLine().split(" ");

            if (line[0].contains("INSERT")){
                String name = line[1];
                int x = Integer.valueOf(line[2]);
                int y = Integer.valueOf(line[3]);
                int z = Integer.valueOf(line[4]);
                insert(name, x, y, z);
            }
            else if (line[0].contains("DELETE")){
                String name = line[1];
                delete(name);
            }
            else if (line[0].contains("BBX")){
                getBox();
            }

            n--;
        }

        input.close();

    }
    
}