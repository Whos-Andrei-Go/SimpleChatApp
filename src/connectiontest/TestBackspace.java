/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectiontest;

/**
 *
 * @author User
 */
public class TestBackspace 
{ 
  public static void main(String[] args) 
  { 
    System.out.print("Line one"); 
    try 
    { 
      Thread.sleep(3000); 
    } catch (InterruptedException e) {} 
    System.out.println("\b\b\btwo"); 
  } 
}
