package cmu.arktweetnlp;

import cmu.arktweetnlp.impl.Model;
import cmu.arktweetnlp.impl.ModelSentence;
import cmu.arktweetnlp.impl.Sentence;
import cmu.arktweetnlp.impl.features.FeatureExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Tagger object -- wraps up the entire tagger for easy usage from Java.
 * <p>
 * To use:
 * <p>
 * (1) call loadModel().
 * <p>
 * (2) call tokenizeAndTag() for every tweet.
 * <p>
 * See main() for example code.
 * <p>
 * (Note RunTagger.java has a more sophisticated runner.
 * This class is intended to be easiest to use in other applications.)
 */
public class Tagger {
    public Model model;
    public FeatureExtractor featureExtractor;

    /**
     * Illustrate how to load and call the POS tagger.
     * This main() is not intended for serious use; see RunTagger.java for that.
     **/
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Supply the model filename as first argument.");
        }
        String modelFilename = args[0];

        Tagger tagger = new Tagger();
        tagger.loadModel(modelFilename);

        String text = "RT @DjBlack_Pearl: wat muhfuckaz wearin 4 the lingerie party?????";
        List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(text);

        for (TaggedToken token : taggedTokens) {
            System.out.printf("%s\t%s\n", token.tag, token.token);
        }
    }

    /**
     * Loads a model from a file.  The tagger should be ready to tag after calling this.
     *
     * @param modelFilename
     * @throws IOException
     */
    public void loadModel(String modelFilename) throws IOException {
        model = Model.loadModelFromText(modelFilename);
        featureExtractor = new FeatureExtractor(model, false);
    }

    /**
     * Run the tokenizer and tagger on one tweet's text.
     **/
    public List<TaggedToken> tokenizeAndTag(String text) {
        if (model == null) throw new RuntimeException("Must loadModel() first before tagging anything");
        List<String> tokens = Twokenize.tokenizeRawTweetText(text);

        Sentence sentence = new Sentence();
        sentence.tokens = tokens;
        ModelSentence ms = new ModelSentence(sentence.T());
        featureExtractor.computeFeatures(sentence, ms);
        model.greedyDecode(ms, false);

        ArrayList<TaggedToken> taggedTokens = new ArrayList<TaggedToken>();

        for (int t = 0; t < sentence.T(); t++) {
            TaggedToken tt = new TaggedToken();
            tt.token = tokens.get(t);
            tt.tag = model.labelVocab.name(ms.labels[t]);
            taggedTokens.add(tt);
        }

        return taggedTokens;
    }

    /**
     * One token and its tag.
     **/
    public static class TaggedToken {
        public String token;
        public String tag;
    }

}
