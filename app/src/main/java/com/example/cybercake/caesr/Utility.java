package com.example.cybercake.caesr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SeterraNova on 2015-03-25.
 */
public class Utility
{
    private final String LOG_TAG = Utility.class.getSimpleName();

    private String savedModule = "";

    private String year = "";

    public void saveModule(String module)
    {
        savedModule = module;
    }

    public String getModule()
    {
        return savedModule;
    }

    //gets the HTML from the URL passed from the passed url
    //returning the page content as a String
    public String fetchHTML(String url) throws MalformedURLException, IOException
    {
        URL csPage;
        URLConnection connection;
        BufferedReader in = null;

        StringBuilder builder = null;

        //System.out.println("URL: " + url);

        try
        {
            csPage = new URL(url);

            connection = csPage.openConnection();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));

            String inputLine;

            builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                builder.append(inputLine.trim());
            }
        }
        finally
        {
            if(in != null)
            {
                in.close();
            }
            if(builder != null) {
                builder.append("");
            }
        }

        return builder.toString().substring(2125);
    }

    //Retrieves available module TITLES and DESCRIPTIONS
    //-TITLE
    //-DESCRIPTION
    //-SUBSCRIBED [DEFAULT TRUE]
    public ArrayList<String> getModules(String pageContent, String preferredModules) throws MalformedURLException, IOException
    {
        ArrayList<String> modules = new ArrayList<>();
        int lastIndex = 0;
        int bIndex = 0;
        int eIndex = 0;

        int bbIndex = 0;
        int eeIndex = 0;

        if(pageContent.length() != 0)
        {
            String addTo = "";
            int last = pageContent.lastIndexOf("</a></strong>");
            boolean subscribed = false;

            while(eIndex != last)
            {
                //MODULE
                lastIndex = eIndex;
                bIndex = pageContent.indexOf("<strong><a", lastIndex)+10;
                bIndex = pageContent.indexOf(">", bIndex)+1;

                eIndex = pageContent.indexOf("</a></strong></td><td >", bIndex);

                if(eIndex == -1)
                {
                    eIndex = pageContent.indexOf("</a></strong></td><td>", bIndex);
                }

                addTo = pageContent.substring(bIndex, eIndex);
                modules.add(addTo);

                subscribed = preferredModules.contains(addTo);
                //DESCRIPTION
                bbIndex = eIndex;

                bbIndex = pageContent.indexOf("<td", bbIndex);
                bbIndex = pageContent.indexOf(">", bbIndex)+1;

                eeIndex = pageContent.indexOf("<br />", bbIndex);

                addTo = pageContent.substring(bbIndex, eeIndex);
                modules.add(addTo);
                //SUBSCRIBED
                if(subscribed)
                {
                    modules.add("true");
                }
                else
                {
                    modules.add("false");
                }
            }
        }
        else
        {
            modules.add(" ");
        }
        return modules;
    }

    //gets all module announce ments from module webpage
    //returns ArrayList containing
    //-MODULE
    //-ANNOUNCEMENT TITLE
    //-AUTHOR
    //-DATE
    //-TIME
    //-ANNOUNCEMENT BODY
    public ArrayList<String> getModuleAnnouncements(String pageContent, String module)
    {
        ArrayList<String> anouncements = new ArrayList<>();

        if(pageContent.contains("No Current Announcements")
                || pageContent.length() == 0)
        {
            //anouncements.add(module);
            anouncements.add("No Anouncements.");
            anouncements.add("none");
            anouncements.add("0");
            anouncements.add("00:00");
            anouncements.add("none");

            return anouncements;
        }

        int lastIndex = 0;
        int bIndex = 0;
        int eIndex = 0;

        if(pageContent.length() != 0)
        {
            String tempContent = pageContent;
            String addTo = "";
            int last = tempContent.lastIndexOf("<div class=\"body\">");
            last = tempContent.indexOf("</div>",last);

            while(eIndex < last)
            {
                //MODULE
                //anouncements.add(module);
                //ANNOUNCEMENT HEADER
                lastIndex = eIndex;
                bIndex = tempContent.indexOf("<h3>", lastIndex)+4;
                eIndex = tempContent.indexOf("</h3>", bIndex);
                addTo = tempContent.substring(bIndex, eIndex);
                anouncements.add(addTo);
                //DETAILS
                int tempIndex = eIndex;
                lastIndex = eIndex;
                bIndex = tempContent.indexOf("<div class=\"description\">", lastIndex);
                bIndex = tempContent.indexOf("</strong>", bIndex)+9;
                eIndex = tempContent.indexOf("</div>", bIndex);
                String postDetails = tempContent.substring(bIndex, eIndex);
                //AUTHOR
                bIndex = 0;
                eIndex = postDetails.indexOf(" on ", bIndex);
                addTo = postDetails.substring(bIndex, eIndex);
                anouncements.add(addTo);

                if(postDetails.contains("last modified on"))
                {
                    postDetails = postDetails.substring(postDetails.indexOf(" on ", eIndex+4));
                    eIndex = 0;
                }

                //DATE
                lastIndex = eIndex;
                bIndex = lastIndex+4;
                eIndex = postDetails.indexOf(", ", bIndex);
                addTo = Long.toString(stringDateToInt(postDetails.substring(bIndex, eIndex)));
                anouncements.add(addTo);
                //TIME
                lastIndex = eIndex;
                bIndex = lastIndex+2;
                eIndex = bIndex+5;
                addTo = postDetails.substring(bIndex, eIndex);
                anouncements.add(addTo);

                //BODY
                lastIndex = tempIndex;
                bIndex = tempContent.indexOf("<div class=\"body\">", lastIndex)+18;
                eIndex = tempContent.indexOf("</div>", bIndex);
                addTo = tempContent.substring(bIndex, eIndex);

                while(addTo.contains("<br />"))
                {
                    addTo = addTo.replace("<br />", "\n");
                }
                anouncements.add(addTo);
            }
        }

        return anouncements;
    }

    //gets the ACTIVE ASSIGNMENTS from the UNDERDRAD/HONORS/MASTERS pages
    //returning the ArrayList<String> containing:
    //-MODULE
    //-ASSIGNMENT
    //-EXPIRY DATE
    //-EXPIRY TIME
    public ArrayList<String> getModuleAssignments(String pageContent, String module)
    {
        ArrayList<String> activeA = new ArrayList<>();
        int lastIndex = 0;
        int bIndex = 0;
        int eIndex = 0;

        if(pageContent.length() != 0)
        {


            if(!pageContent.contains("<div class=\"padded\">No currently active assignments</div>") || pageContent.contains("<h1>Fitchfork Assignments</h1>"))
            {
                String addTo = "";
                int bLimit = bIndex = pageContent.lastIndexOf("<h1>Active Assignments</h1>");
                int last = pageContent.lastIndexOf("</span></td></tr></table><input type=\"hidden\" name=\"formID\" value=\"Upload Assignment\"/");
                if (pageContent.contains("<h1>Fitchfork Assignments</h1>"))
                {

                    bLimit = bIndex = pageContent.lastIndexOf("Select Fitchfork Assignment:");
                    last = pageContent.lastIndexOf("</td></tr></table><input type=\"hidden\" name=\"moduleCode\"")-100;

                }
                //</strong><br><span class="description"><strong>
                while(eIndex < last && bIndex >= bLimit)
                {
                    //MODULE
                    //activeA.add(module);
                    //ASSIGNMENT DISCR
                    bIndex = pageContent.indexOf("<td><strong>", eIndex)+12;
                    eIndex = pageContent.indexOf("</strong><br />", bIndex);
                    addTo = pageContent.substring(bIndex, eIndex);
                    if(addTo.length() > 200 || activeA.contains(addTo))
                    {
                        break;
                    }
                    activeA.add(addTo);
                    //DATE
                    bIndex = pageContent.indexOf("Due: </strong>", eIndex)+14;
                    eIndex = pageContent.indexOf("at", bIndex)-1;
                    addTo = Long.toString(stringDateToInt(pageContent.substring(bIndex, eIndex)));
                    if(addTo.length() > 200)
                    {
                        break;
                    }
                    activeA.add(addTo);
                    //TIME
                    bIndex = eIndex+4;
                    eIndex = bIndex+5;
                    addTo = pageContent.substring(bIndex, eIndex);
                    if(addTo.length() > 200)
                    {
                        break;
                    }
                    activeA.add(addTo);
                }
            }
            else
            {
                //activeA.add(module);
                activeA.add("There are currently no assignments due.");
                activeA.add("0");
                activeA.add("00:00");
            }
        }
        else
        {
            //activeA.add(module);
            activeA.add("There are currently no assignments due.");
            activeA.add("0");
            activeA.add("00:00");
        }

        return activeA;
    }

    //gets all module discussions from module webpage
    //returns ArrayList containing
    //-MODULE
    //-DISCUSSION TITLE
    //-MODULE PAGE LINK
    //-LAST UPDATED DATE
    //-LAST UPDATED TIME
    public ArrayList<String> getModuleDiscussions(String pageContent, String module)
    {
        ArrayList<String> discussions = new ArrayList<>();

        if(pageContent.contains("This module has no active discussions")
                || !pageContent.contains("Active Discussions"))
        {
            //discussions.add(module);
            discussions.add("No Discussions.");
            discussions.add("http://www.cs.up.ac.za/courses/"+module);
            discussions.add("0");
            discussions.add("00:00");

            return discussions;
        }

        int lastIndex = 0;
        int bIndex = 0;
        int eIndex = 0;

        if(pageContent.length() != 0)
        {
            String tempContent = pageContent;
            String addTo = "";
            int last = tempContent.lastIndexOf("<h1>Module Links</h1>");

            eIndex = tempContent.indexOf("<h1>Active Discussions</h1>");

            while(eIndex < last)
            {
                //MODULE
                //discussions.add(module);

                //DISCUSSION TITLE
                lastIndex = eIndex;
                bIndex = tempContent.indexOf("<a href=\"http://www.cs.up.ac.za/discussions/topic/", lastIndex);

                if(bIndex == -1)
                {
                    break;
                }
                bIndex = tempContent.indexOf(">", bIndex)+1;

                eIndex = tempContent.indexOf("</a></strong></div>", bIndex);
                addTo = tempContent.substring(bIndex, eIndex);
                discussions.add(addTo);
                //MODULE PAGE LINK
                discussions.add("http://www.cs.up.ac.za/courses/"+module);
                //DATE
                lastIndex = eIndex;
                bIndex = tempContent.indexOf("Updated: </strong>", lastIndex)+18;
                eIndex = tempContent.indexOf(",", bIndex);
                addTo = Long.toString(stringDateToInt(tempContent.substring(bIndex, eIndex)));
                discussions.add(addTo);
                //TIME
                bIndex = eIndex+2;
                eIndex = bIndex+5;
                addTo = tempContent.substring(bIndex, eIndex);
                discussions.add(addTo);
            }
        }
        else
        {
            discussions.add("No Discussions.");
            discussions.add("http://www.cs.up.ac.za/courses/"+module);
            discussions.add("0");
            discussions.add("00:00");
        }

        return discussions;
    }

    //gets all module CONTENT from module webpage
    //returns ArrayList<ArrayList<String>> containing
    //-MODULE-X
    //-CONTENT CATEGORY-X
    //-CONTENT TITLE
    //-CONTENT LINK
    //-CONTENT SIZE
    public ArrayList<String> getModuleContent(String pageContent, String module)
    {
        ArrayList<String> moduleContent = new ArrayList<>();

        int lastIndex = 0;
        int bIndex = 0;
        int eIndex = 0;

        if(pageContent.contains("folderMinus")
            || pageContent.length() != 0)
        {
            String tempContent = pageContent;
            String addTo = "";
            String category = "";

            int last = tempContent.lastIndexOf("</table></div><table cellspacing=\"0\">");

            int theB = bIndex = eIndex = tempContent.indexOf("<h1>Module Content</h1>");

            if(last - eIndex < 200)
            {
                //moduleContent.add(module);
                moduleContent.add("http://www.cs.up.ac.za/courses/"+module);
                moduleContent.add("No Content");
                moduleContent.add("n/a");
                return moduleContent;
            }

            boolean notLoop = true;
            Set<Integer> counter = new HashSet<>();
            ArrayList<String> temp = new ArrayList<>();

            //while(eIndex > 1000 && eIndex < last)
            while(bIndex != -1 && notLoop && eIndex < last)
            {
                //CONTENT STRING
                lastIndex = eIndex;
                bIndex = tempContent.indexOf("class=\"file\"", eIndex);

                eIndex = tempContent.indexOf("</div>", bIndex);
                notLoop = counter.add(bIndex);
                notLoop = counter.add(eIndex);
//System.out.println(bIndex+"|"+eIndex + "-" +last + "#" + tempContent.indexOf("class=\"file\"") + " " + module);
                if(bIndex == -1)
                {break;}
                temp = getContent(tempContent.substring(bIndex, eIndex), module);
                //ADD TO MODULE CONTENT
                for (int i = 0; i < temp.size(); i++)
                {
                    moduleContent.add(temp.get(i));
// System.out.println(temp.get(i));
                }
            }
        }
        else
        {
            //moduleContent.add(module);
            moduleContent.add("http://www.cs.up.ac.za/courses/"+module);
            moduleContent.add("No Content");
            moduleContent.add("n/a");
        }
        return moduleContent;
    }

    private ArrayList<String> getContent(String contentIn, String module)
    {
        ArrayList<String> content = new ArrayList<>();

        int bIndex = 0;
        int eIndex = 0;
        int last = contentIn.indexOf("</span></td></tr>");

        String addTo = "";

        while(eIndex < last)
        {
            //MODULE
            //content.add(module);
            //CATAGORY
            //content.add(category);
            //LINK
            bIndex = contentIn.indexOf("<a href=\"", eIndex)+9;
            eIndex = contentIn.indexOf("\">", bIndex);
            addTo = contentIn.substring(bIndex, eIndex);
            content.add(addTo);
            //FILE NAME
            bIndex = eIndex+2;
            eIndex = contentIn.indexOf("</a>", bIndex);
            addTo = contentIn.substring(bIndex, eIndex);
            content.add(addTo);
            //FILE SIZE
            bIndex = contentIn.indexOf("fileSize\">", eIndex)+10;
            eIndex = contentIn.indexOf("</span><br />", bIndex);
            addTo = contentIn.substring(bIndex, eIndex);
            content.add(addTo);
            eIndex = contentIn.indexOf("</span>", eIndex+10);
        }

        return content;
    }

    private long stringDateToInt(String date)
    {
        if(date.contains("Jan"))
        {
            date = date.replace("January", "01");
            date = date.replace("Jan", "01");
        }
        else if(date.contains("Feb"))
        {
            date = date.replace("February", "02");
            date = date.replace("Feb", "02");
        }
        else if(date.contains("Mar"))
        {
            date = date.replace("March", "03");
            date = date.replace("Mar", "03");
        }
        else if(date.contains("Apr"))
        {
            date = date.replace("April", "04");
            date = date.replace("Apr", "04");
        }
        else if(date.contains("May"))
        {
            date = date.replace("May", "05");
        }
        else if(date.contains("Jun"))
        {
            date = date.replace("June", "06");
            date = date.replace("Jun", "06");
        }
        else if(date.contains("Jul"))
        {
            date = date.replace("July", "07");
            date = date.replace("Jul", "07");
        }
        else if(date.contains("Aug"))
        {
            date = date.replace("Aughust", "08");
            date = date.replace("Aug", "08");
        }
        else if(date.contains("Sep"))
        {
            date = date.replace("September", "09");
            date = date.replace("Sep", "09");
        }
        else if(date.contains("Oct"))
        {
            date = date.replace("October", "10");
            date = date.replace("Oct", "10");
        }
        else if(date.contains("Nov"))
        {
            date = date.replace("November", "11");
            date = date.replace("Nov", "11");
        }
        else if(date.contains("Dec"))
        {
            date = date.replace("December", "12");
            date = date.replace("Dec", "12");
        }

        if(date.length() < 8)
        {
            date += " " + year;
        }
        else
        {
            year = date.substring(date.length()-4);
        }

        date = date.replaceAll(" ", "");

        if(date.length() < 8)
        {
            date = "0"+date;
        }

        SimpleDateFormat f = new SimpleDateFormat("ddmmyyyy");
        Date d = null;
        try {
            d = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = d.getTime();
        //System.out.println();
        return milliseconds;
    }

    //read all raw module pages into a single array
    public ArrayList<String> collectAllPages(String url, ArrayList<String> modules)
    {
        ArrayList<String> modulePages = new ArrayList<>();
        for(int i = 0; i < modules.size(); i+=3)
        {try {
                    modulePages.add(fetchHTML(url + modules.get(i)));
                } catch (IOException e) {
                    e.printStackTrace();

            }
        }

        return modulePages;
    }

    //read all module announcements of specific academic level into a 2D array
    public ArrayList<ArrayList<String>> collectAllAnnouncements(ArrayList<String> modulePages, ArrayList<String> modules)
    {
        ArrayList<ArrayList<String>> announcements = new ArrayList<>();
        int j = 0;
        for(int i = 0; i < modulePages.size();i++)
        {
            announcements.add(getModuleAnnouncements(modulePages.get(i), modules.get(j)));
            j += 3;
        }

        return announcements;
    }

    //read all module assignments of specific academic level into a 2D array
    public ArrayList<ArrayList<String>> collectAllAssignments(ArrayList<String> modulePages, ArrayList<String> modules)
    {
        ArrayList<ArrayList<String>> assignments = new ArrayList<>();
        int j = 0;
        for(int i = 0; i < modulePages.size(); i++)
        {
            assignments.add(getModuleAssignments(modulePages.get(i), modules.get(j)));
            j += 3;
        }

        return assignments;
    }

    //read all module discussions of specific academic level into a 2D array
    public ArrayList<ArrayList<String>> collectAllDiscussions(ArrayList<String> modulePages, ArrayList<String> modules)
    {
        ArrayList<ArrayList<String>> discussions = new ArrayList<>();
        int j = 0;
        for(int i = 0; i < modulePages.size(); i++)
        {
            discussions.add(getModuleDiscussions(modulePages.get(i), modules.get(j)));
            j += 3;
        }

        return discussions;
    }

    //read all moduleContent of specific academic level into a 2D array
    public ArrayList<ArrayList<String>> collectAllModuleContent(ArrayList<String> modulePages, ArrayList<String> modules)
    {
        ArrayList<ArrayList<String>> moduleContent = new ArrayList<>();
        int j = 0;
        for(int i = 0; i < modulePages.size(); i++)
        {
            moduleContent.add(getModuleContent(modulePages.get(i), modules.get(j)));
            j += 3;
        }

        return moduleContent;
    }

    static String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        return format.format(date).toString();
    }
}
