package it.polimi.ingsw.client.cli;

public class test {

    public static void main(String[] args) {
        String ans = "board    ";
        int ansWithoutLetters = Integer.parseInt(ans.replaceAll("[^0-9]", ""));
        String ansWithoutNumbers = ans.replaceAll("[0-9 ]", "");
        System.out.println(ans);
        System.out.println(ansWithoutLetters);
        System.out.println(ansWithoutNumbers);
    }
}