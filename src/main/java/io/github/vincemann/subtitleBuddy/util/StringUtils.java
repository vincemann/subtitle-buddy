package io.github.vincemann.subtitleBuddy.util;


import static com.google.common.base.Preconditions.checkArgument;

public class StringUtils {

    private StringUtils(){}

    public static String findFirstWord(String wordSequence){
        checkArgument(wordSequence!=null);
        String noPrefixSpacesWordSequence = eliminatePrefixSpaces(wordSequence);
        StringBuilder firstWord = new StringBuilder();
        for (int i = 0 ; i<noPrefixSpacesWordSequence.length();i++){
            if(noPrefixSpacesWordSequence.charAt(i)==' ' || (noPrefixSpacesWordSequence.charAt(i)+"").equals("\t") || (noPrefixSpacesWordSequence.charAt(i)+"").equals("\n")){
                return firstWord.toString();
            }else {
                firstWord.append(noPrefixSpacesWordSequence.charAt(i));
            }
        }
        return firstWord.toString();
    }

    public static String findLastWord(String wordSequence){
        checkArgument(wordSequence!=null);
        String noPrefixSpacesWordSequence = eliminateSuffixSpaces(wordSequence);
        StringBuilder lastWord = new StringBuilder();
        for (int i = noPrefixSpacesWordSequence.length()-1; i>=0;i--){
            if(noPrefixSpacesWordSequence.charAt(i)==' '){
                return lastWord.reverse().toString();
            }else {
                lastWord.append(noPrefixSpacesWordSequence.charAt(i));
            }
        }
        return lastWord.reverse().toString();
    }

    private static String eliminateSuffixSpaces(String s){
        StringBuilder stringBuilder = new StringBuilder(s);
        String result = eliminatePrefixSpaces(stringBuilder.reverse().toString());
        return new StringBuilder(result).reverse().toString();
    }

    private static String eliminatePrefixSpaces(String s){
        String resultString = s;
        for(int i = 0; i<s.length();i++){
            if(s.charAt(i)==' '){
                if(i+1>=s.length()){
                    throw new IllegalArgumentException("String does not contain any words");
                }
                resultString = s.substring(i+1);
            }else {
                return resultString;
            }
        }
        return resultString;
    }

}
