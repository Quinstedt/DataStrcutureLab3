import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

// The main plagiarism detection program.
// You only need to change buildIndex() and findSimilarity().
public class PlagiarismDetector {

    public static void main(String[] args) throws IOException {
         // If you don't want to specify arguments on the command-line, just uncomment this block.
        if (args.length == 0) {
            args = new String[] { "documents/medium" }; // Path to the document set.
        }


        // If no arguments are given, ask for them.
        if (args.length == 0) {
            Scanner input = new Scanner(System.in);
            System.out.print("Give the path to the document set: ");
            System.out.flush();
            args = new String[] {input.nextLine()};
        }

        // Read document paths from provided folder name.
        if (args.length != 1) {
            System.err.println("Usage: you have to provide a program argument:");
            System.err.println("  (1) the name of the directory to scan");
            System.exit(1);
        }
        Path[] paths = Files.list(Paths.get(args[0])).toArray(Path[]::new);
        Arrays.sort(paths);

        // Stopwatches time the execution of each phase of the program.
        Stopwatch stopwatch = new Stopwatch();
        Stopwatch stopwatch2 = new Stopwatch();

        // Read all input files.
        BST<Path, Ngram[]> files = readPaths(paths);
        stopwatch.finished("Reading all input files");

        // Build index of n-grams (not implemented yet).
        BST<Ngram, ArrayList<Path>> index = buildIndex(files);
        stopwatch.finished("Building n-gram index");

        // Compute similarity of all file pairs.
        BST<PathPair, Integer> similarity = findSimilarity(files, index);
        stopwatch.finished("Computing similarity scores");

        // Find most similar file pairs, arranged in decreasing order of similarity.
        ArrayList<PathPair> mostSimilar = findMostSimilar(similarity);
        stopwatch.finished("Finding the most similar files");
        stopwatch2.finished("In total the program");
        System.out.println();

        // Print out some statistics.
        System.out.println("Balance statistics:");
        System.out.println("  files: " + files.statistics());
        System.out.println("  index: " + index.statistics());
        System.out.println("  similarity: " + similarity.statistics());
        System.out.println();

        // Print out the plagiarism report!
        System.out.println("Plagiarism report:");
        mostSimilar.stream().limit(50).forEach((PathPair pair) -> {
            System.out.printf("%5d similarity: %s%n", similarity.get(pair), pair);
        });
    }

    // Phase 1: Read in each file and chop it into n-grams.
    static BST<Path, Ngram[]> readPaths(Path[] paths) throws IOException {
        BST<Path, Ngram[]> files = new BST<Path, Ngram[]>();
        for (Path path: paths) {
            String contents = new String(Files.readAllBytes(path));
            Ngram[] ngrams = Ngram.ngrams(contents, 5);
            // Remove duplicates from the ngrams list
            // Uses the Java 8 streams API - very handy Java feature
            // which we don't cover in the course. If you want to
            // learn about it, see e.g.
            // https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#package.description
            // or https://stackify.com/streams-guide-java-8/
            ngrams = Arrays.stream(ngrams).distinct().toArray(Ngram[]::new);
            files.put(path, ngrams);
        }

        return files;
    }

    // Phase 2: Build index of n-grams (not implemented yet).
    static BST<Ngram, ArrayList<Path>> buildIndex(BST<Path, Ngram[]> files) {
        /** PARAMETOR
         *  BST: key: path       Value: Ngram[] called files */
        BST<Ngram, ArrayList<Path>> index = new BST<Ngram, ArrayList<Path>>();
        /** we create a BST:     key: Ngram      Value: Arraylist called index */
        for(Path path: files){
            /** we go through the BST files */
            Ngram[] ngram = files.get(path);
            /** we insert the key path and get the value and insert it in Ngram[] ngram */
            for(Ngram currentNgram : ngram){
                /** we go through the Ngram[] */
                if( index.containsKey(currentNgram)){
                    /** if the BST  index contains the current key */
                        index.get(currentNgram).add(path);
                        /** we add that to the arraylist ( which is the value) */
                    } else {
                    /** if the hashmap doens not contain the key:
                         we create a new arrayList */
                    ArrayList<Path>  newPath = new ArrayList<>();
                    newPath.add(path);
                    /** we add the path to the Arraylist */
                    index.put(currentNgram, newPath);
                    /** we create a key with the current Ngram and give it the path
                         by  using the created ArrayList and the path inside */
                }
            }
        }
      return index;
    }

    // Phase 3: Count how many n-grams each pair of files has in common.
    static BST<PathPair, Integer> findSimilarity(BST<Path, Ngram[]> files, BST<Ngram, ArrayList<Path>> index) {
        /** PARAMETER IN THE METHOD
             BTS: key:path      value: ngram[] (called files)
            and
            BST: key: Ngram      value: Arraylist path ( called index) */

        // TODO: Use index to make this loop much more efficient.
        // N.B. Path is Java's class for representing filenames.
        // PathPair represents a pair of Paths (see PathPair.java).

        BST<PathPair, Integer> similarity = new BST<PathPair, Integer>();
        // create a BST:  key: pathPair     Value: integer which represent the amount of n-grams each pair


        /**  for each loops: here we are using the Key: Path and going through the BST files*/
         /*   for (Path path1: files) {
              for (Path path2: files) {     */
        /** if both paths are equal we continue;*/
            /*    if (path1.equals(path2))
                    continue;*/
     /**    we insert the value of files = Ngram[] and copy to Ngram[] ngram1. Same for Ngram[] ngram2 */
     /*
                Ngram[] ngrams1 = files.get(path1);
                Ngram[] ngrams2 = files.get(path2);     */

      /** we iterate trough Ngram[] and get the value of ngram and check if it is equal to ngram2. */
       /*         for (Ngram ngram1: ngrams1) {
                    for (Ngram ngram2: ngrams2) {
                        if (ngram1.equals(ngram2)) { */
        /** if there are equal we create a PathPair and insert both path in it. */
                       //     PathPair pair = new PathPair(path1, path2);
       /** if the BST similarity does not contains key "pair" we create a node
            we use "pair" as a key and assign 0 to the value */
                /*       if (!similarity.containsKey(pair))
                                similarity.put(pair, 0);*/
       /** if the key "pair" exist: We insert the same key: "pair" and insert a new value
             the new value is BTSs pair value +1 */
               /*             similarity.put(pair, similarity.get(pair)+1);
                        }
                    }
                }
            }
        }

        */
        /** we go through the BST index, instead of BST files
             this gives one less for each loop
         * instead of starting with going through the BST files that have <path,Ngram[]>
         * I go use the BST <Ngram, Arraylist<Path>> like I did in the buildIndexMethod.
         *
         *
         */
        for(Ngram ngram: index){
            ArrayList<Path> paths = index.get(ngram);
            for(Path path1: paths) {
                for (Path path2 : paths) {
                    if (path1 == path2)
                        continue;
        /** Inserted the value in a PathPair */
                    PathPair pair = new PathPair(path1 ,path2);
                    if (!similarity.containsKey(pair)) {
        /** Used the same as above */
                        similarity.put(pair, 0);
                    } else {
        /** Used the same as above
         * +1 is because we are using the index and we need to take to account index 0.
         *  */
                        similarity.put(pair, similarity.get(pair) + 1);
                    }
                }
            }
        }
        return similarity;
    }

    // Phase 4: find all pairs of files with more than 30 n-grams
    // in common, sorted in descending order of similarity.
    static ArrayList<PathPair> findMostSimilar(BST<PathPair, Integer> similarity) {
        // We use the Java 8 streams API - see the comment to the
        // 'readPaths' method for more information.
        return
            // Convert allPathPairs into a stream. This is a bit more
            // complicated than it should be, because BST doesn't
            // implement the streaming API. If BST came from the Java
            // standard library, we could just write
            // 'allPathPairs.stream()' or 'similarity.keys().stream()'.
            StreamSupport.stream(similarity.spliterator(), false)
            // Keep only dictinct pairs with more than 30 n-grams in common.
            .filter(pair -> !pair.path1.equals(pair.path2) && similarity.get(pair) >= 30)
            // Remove duplicates - pairs (path1, path2) and (path2, path1).
            .map(PathPair::canonicalise)
            .distinct()
            // Sort to have the most similar pairs first.
            .sorted(Comparator.comparing(similarity::get).reversed())
            // Store the result in an ArrayList.
            .collect(Collectors.toCollection(ArrayList<PathPair>::new));
    }

}
