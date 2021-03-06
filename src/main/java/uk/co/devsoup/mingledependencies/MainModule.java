package uk.co.devsoup.mingledependencies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MainModule {
    public static final Logger LOGGER = LoggerFactory.getLogger(MainModule.class);

    public static void main(String args[]) {
        boolean success = new MainModule().run(args);
        if (success) {
            System.exit(0);
        }
        System.exit(-1);
    }

    public boolean run(final String args[]) {
        LOGGER.info("Application startup");
        boolean successfulExecution = false;

        Set<Integer> storiesToProcess = null;

        boolean canProcess = false;
        if (args != null && args.length > 0) {
            String action = args[0];
            if (action.compareToIgnoreCase("stories") == 0) {
                storiesToProcess = parseStories(args[1]);
                canProcess = true;
            } else if (action.compareToIgnoreCase("storyRange") == 0) {
                storiesToProcess = parseStoryRange(args[1]);
                canProcess = true;
            }
        }

        if (canProcess) {
            Map<Integer, StoryDetails> storyDetails = new HashMap<Integer, StoryDetails>();
            Map<Integer, List<Integer>> dependencies = new HashMap<Integer, List<Integer>>();
            boolean success = new Mingle().generateDependencies(storiesToProcess, storyDetails, dependencies);
            if (success) {
                new DotFileGenerator().generateOutput(storyDetails, dependencies);
                successfulExecution = true;
            }
        } else {
            printHelp();
        }
        LOGGER.info("Application shutdown");
        return successfulExecution;
    }

    private void printHelp() {
        System.out.println("Parse Mingle for Depends_Upon(#x,#y) in the story description, and generates a dependency graph file for GraphViz.");
        System.out.println("");
        System.out.println("Parameters:-");
        System.out.println("  stories <story1>,<story2> - queries the given stories (eg 'stories 1234,2345')");
        System.out.println("  storyRange <story1>,<story2> - queries the stories inclusive (eg 'stories 1,9999')");
    }

    private Set<Integer> parseStories(final String stringToProcess) {
        Set<Integer> result = new TreeSet<Integer>();

        String[] split = stringToProcess.split(",");
        for (String singleString : split) {
            result.add(Integer.parseInt(singleString));
        }

        return result;
    }

    private Set<Integer> parseStoryRange(final String stringToProcess) {
        Set<Integer> result = new TreeSet<Integer>();

        String[] split = stringToProcess.split(",");
        int start = Integer.parseInt(split[0]);
        int end = Integer.parseInt(split[1]);
        for (int counter = start; counter <= end; ++counter) {
            result.add(counter);
        }

        return result;
    }


}
