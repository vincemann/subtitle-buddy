package com.youneedsoftware.subtitleBuddy.util;


import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class StringUtils {

    private StringUtils(){}

    public static String trimFileEnding(String fileName){
        StringBuilder suffix = new StringBuilder();
        for(int i = fileName.length()-1;i>0;i--){
            if(fileName.charAt(i)=='.'){
                return suffix.reverse().toString();
            }
            suffix.append(fileName.charAt(i));
        }
        return suffix.reverse().toString();
    }


    public static String getLastDirectiveOfPath(String path){
        StringBuilder suffix = new StringBuilder();
        for(int i = path.length()-1;i>0;i--){
            if(path.charAt(i)=='/'){
                return suffix.reverse().toString();
            }
            suffix.append(path.charAt(i));
        }
        return suffix.reverse().toString();
    }

    public static String removeAllNewLines(String s){
        return s.replace("\n", "").replace("\r", "");
    }

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

    public static List<String> getWordsOfWordSequence(String s){
        String[] words = s.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        return Arrays.asList(words);
    }


    /*public static String findWordsOfString(String wordSequence){
        List<String> words = new ArrayList<>();
        StringBuilder currWord = new StringBuilder();
        for (int i = 0 ; i<wordSequence.length();i++){
            if(wordSequence.charAt(i)==' ' || wordSequence.charAt(i)+"".equals(System.lineSeparator())){
                words.add(currWord.toString());
                currWord.setLength(0);
            }else {
                currWord.append(wordSequence.charAt(i));
            }
        }
        throw new IllegalArgumentException("No word found in sequence");
    }*/

}
