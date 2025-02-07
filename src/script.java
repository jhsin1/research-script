package src;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 


public class script
{ 
    public static void main(String[] args) throws Exception  
    { 
        List<Map<String, Set<String>>> listofMaps = new ArrayList<>();
        for (int i = 0; i < 6; i++) listofMaps.add(new HashMap<>());

        File folder = new File("files");
        for (File file : folder.listFiles())
        {
            String fn = "files/" + file.getName();
            List<List<String>> list = conceptListGenerator(fn);
            for (int i = 0; i < list.size(); i++) // i = course orders, which should correspond to the map # 
            {
                List<String> listofConcepts = list.get(i);
                Map<String, Set<String>> map = listofMaps.get(i);
                for (String concept : listofConcepts)
                {
                    if (map.containsKey(concept))
                    {
                        map.get(concept).add(fn.substring(6, fn.length()-6));
                    }
                    else
                    {
                        Set<String> la = new HashSet<>();
                        la.add(fn.substring(6, fn.length()-6)); 
                        map.put(concept, la);
                    }
                }
            }
        }


        // for contention
        for (int i = 0; i < 1; i++)
        {
            for (String key : listofMaps.get(i).keySet()) {
                if (listofMaps.get(i).get(key).size() == 3) {
                    String s = String.format("%-30s", key);
                    System.out.println("Key: " + s + ", Annotators: " + listofMaps.get(i).get(key).toString());
                }
            }
        }

        // all
        
        // for (String key : listofMaps.get(2).keySet()) {
        //     String s = String.format("%-30s", key);
        //     System.out.println("Key: " + s + ", Annotators: " + listofMaps.get(2).get(key).toString());
        // }



        // testing

        // List<List<String>> list = conceptListGenerator("files/jessica.jsonl");

        // System.out.println(list.get(1).toString());
    } 

    private static List<List<String>> conceptListGenerator(String filename) throws Exception
    {
        List<List<String>> res = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename, java.nio.charset.StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null)
        {
            List<String> list = new ArrayList<>();
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj; 

            String text = (String) jo.get("text"); 
            JSONArray concepts = (JSONArray) jo.get("label");

            for (int i = 0; i < concepts.size(); i++)
            {
                JSONArray concept = (JSONArray) concepts.get(i);
                long i1 = (long) concept.get(0);
                long i2 = (long) concept.get(1);
                list.add(text.substring((int) i1, (int) i2));
            }
            res.add(list);
        }

        return res;
    }
} 