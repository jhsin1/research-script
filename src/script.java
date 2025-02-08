package src;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class script { 
    public static void main(String[] args) throws Exception { 
        Map<Integer, Map<String, Set<String>>> map_of_courses = new HashMap<>();
        File folder = new File("files");
        File[] f_direct = folder.listFiles();

        for (File file : f_direct) {
            String fn = "files/" + file.getName();
            Map<Integer, List<String>> annotation_list = conceptListGenerator(fn); // 

            for (Integer key_id : annotation_list.keySet()) {
                List<String> listofConcepts = annotation_list.get(key_id);

                if (map_of_courses.containsKey(key_id)) { // THERE IS A MAP<STRING, SET<STRING>>
                    for (String concept : listofConcepts) {
                        if (map_of_courses.get(key_id).containsKey(concept))
                            map_of_courses.get(key_id).get(concept).add(fn.substring(6, fn.length()-6)); // add the name of annotator to this concept list
                        else
                        {
                            Set<String> la = new HashSet<>(); // la for list of annotators
                            la.add(fn.substring(6, fn.length()-6)); 
                            map_of_courses.get(key_id).put(concept, la);
                        }
                    }
                }
                else { // IF THERE ISNT A MAP<STRING, SET<STRING>>
                    Map<String, Set<String>> map = new HashMap<>();
                    for (String concept : listofConcepts) {
                        if (map.containsKey(concept))
                            map.get(concept).add(fn.substring(6, fn.length()-6)); // add the name of annotator to this concept list
                        else
                        {
                            Set<String> la = new HashSet<>(); // la for list of annotators
                            la.add(fn.substring(6, fn.length()-6)); 
                            map.put(concept, la);
                        }
                    }

                    map_of_courses.put(key_id, map);
                }
            }
        }

        //

        BufferedWriter writer = new BufferedWriter(new FileWriter("with_contention_medium.txt"));

        // printing test
        for (int i = 290; i <= 295; i++)
        {
            Map<String, Set<String>> mp = map_of_courses.get(i);
            writer.write("\nFiltered by contention level medium (2-to-4 or 4-to-2)\nCourse ID: " + i + "\n\n");
            System.out.println("Course ID: " + i);
            for (String key : mp.keySet()) {
                if (map_of_courses.get(i).get(key).size() == 2 || map_of_courses.get(i).get(key).size() == 4) { // this is for contention!! // commment this statement out for the whole list
                    String s = String.format("%-30s", key); 
                    String text = "Key: " + s + ", Annotators: " + mp.get(key).toString();
                    writer.write(text + "\n");
                    System.out.println(text);
                }
            }
        }

        writer.close();
    }

    private static Map<Integer, List<String>> conceptListGenerator(String filename) throws Exception
    {
        Map<Integer, List<String>> res = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(filename, java.nio.charset.StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null)
        {
            List<String> list = new ArrayList<>();
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj; 

            long id = (long) jo.get("id");
            String text = (String) jo.get("text"); 
            JSONArray concepts = (JSONArray) jo.get("label");

            for (int i = 0; i < concepts.size(); i++)
            {
                JSONArray concept = (JSONArray) concepts.get(i);
                long i1 = (long) concept.get(0);
                long i2 = (long) concept.get(1);
                String c = text.substring((int) i1, (int) i2);
                list.add(c); // adds the concept to list
            }
            res.put((int) id, list);
        }

        return res;
    }
} 
