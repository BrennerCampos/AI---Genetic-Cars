/*
	MOST OF YOUR CODE WILL GO IN THIS CLASS

	This is the main class for your project.  It does the following:
		- constructs KILLTOPOPULATION random cars
		- runs GENERATIONS breed/race/kill/mutate generations to evolve a car that completes the racetrack
		- shows the resulting car

	A generation consists of the following:
		- breed: mate pairs of cars, with probability BREED_RATE, adding the resulting cars to the population
		- race every car.  for each car, make a simulated world, run the car for ITERATIONS frames, then score it
			- cars are scored first by distance traveled.  Further is better.
			- cars that make the end of the track (position of 500) are scored second by iterations, or time taken to reach the end.  Smaller is better.
		- kill: sort the cars by score, and keep only the top KILLTOPOPULATION
		- mutate: each car, with probability MUTATE_SELECTION_RATE, has the chance to produce a new mutant car that is added to the population

	YOU SHOULD WRITE, AT A MINIMUM, FUNCTIONS BREED() KILL() and MUTATE().  Find the TODO lines.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GeneticCars implements MouseListener
{
	//GENETIC PARAMETERS

	//how many frames the car is raced for
	public static final int ITERATIONS=2000;

	//number of breed/race/kill/mutate rounds
	public static final int GENERATIONS=100;

	//after each kill round, this many cars are left
	public static final int KILLTOPOPULATION=17;

	//the probability that any two cars will mate in a breed round
	public static final double BREED_RATE=0.41;

	//the probability that any car will produce a baby mutant in a mutate round
	public static final double MUTATE_SELECTION_RATE=0.35;

	//if the mutant is made, the probability that any single ball position or link is altered
	public static final double MUTATE_RATE=0.32;


	public double newCourseChances = 0.2;


	//This arraylist holds the population of cars
	public ArrayList<Car> population;
	public int courseSelected = 0;
	public int switchCounter = 0;
	public int newCourseSwitch = 0;

	//Program starts here:
	// creates an initial population
	// does the genetic simulation
	// shows the winning car

	public GeneticCars()
	{
		population=new ArrayList<Car>();


		generateInitialPopulation(KILLTOPOPULATION);
		doGenetic(GENERATIONS);

		show(population.get(0));
		//showAll();
	}

	// does the genetic simulation
	public void doGenetic(int generations)
	{
		System.out.println("Starting with course "+courseSelected);
		// runs for generations cycles
		for(int g=0; g<generations; g++)
		{

			/*System.out.println("\\--- STARTING GENERATION: "+g+" ---\\");

			// --- Initial Population (BEFORE ANYTHING) debugging
			for (int i=0;i< population.size();i++) {
				System.out.println("Score of car in initial population position "+i+": " + population.get(i).score_position);
			}
			System.out.println();*/

			//calls the breed, race, kill, mutate functions and prints the winner
			breed();


			//  ---- COURSE SWITCHER ------------------

			// If we've gone through at least 20 generations, have the chance to switch courses to a random course
			double random = Math.random();
			//	System.out.println("random generated: "+random);
			if ((newCourseSwitch>=20) && (random < newCourseChances)) {
				while (true) {
					int randRoll = randint(0, 4);
					if (randRoll != courseSelected) {
						courseSelected = randRoll;
						System.out.println("Changing courses to course: " + (courseSelected));
						newCourseSwitch = 0;
						break;
					}
				}
			}

			raceAll();

			// --- BREED DEBUGGING ---------------------------
			/*
			// after new BREED debugging
			for (int i=0;i< population.size();i++) {
				System.out.println("Score in population position "+i+" after breed: " + population.get(i).score_position);
			}
			System.out.println();
			 */


			kill();

			// after KILL debugging
			/*for (int i=0;i< population.size();i++) {
				System.out.println("Score in population position "+i+" after kill: "+population.get(i).score_position);
			}
			System.out.println();*/
			// System.out.println("population for "+(g+1)+": "+ Arrays.toString(population.get(0).score_position));

			mutate();


			newCourseSwitch ++;


			// after MUTATE debugging
			/*for (int i=0;i< population.size();i++) {
				System.out.println("Score in population position "+i+" after mutate: "+population.get(i).score_position);
			}*/

			/*System.out.println();*/
			System.out.println("Generation "+(g+1)+": best car has distance "+population.get(0).score_position+"/500, Iterations "+population.get(0).score_iterations+"/2000");

			/*System.out.println();
			System.out.println("----------------------------------------------------------------");
			System.out.println();
			System.out.println();*/
		}

	}


	//creates n new cars, each with 10 balls and random links, puts them in the population arraylist
	public void generateInitialPopulation(int n)
	{
		for(int i=0; i<n; i++)
			population.add(new Car(10));
	}

	//TODO
	//YOU WRITE THIS
	public void breed()
	{
		// should see more cars if we get this right

		//Make an arraylist of new cars
		ArrayList<Car> bredCars = new ArrayList<>();

		//Car car = new Car(10);
		//bredCars.add(0, car);
		//System.out.println(carsArray);
		int populationSize = population.size();

		// Go through every pair of cars in population
		for(int d = 0; d < populationSize; d++) {
			for (int m = 0; m < populationSize; m++) {

				//only some, chosen at random, get to mate
				//  with probability BREED_RATE, mate them by calling the "breed" method in class Car, and add the child to the new car arraylist
				if (Math.random() < BREED_RATE) {

					// baby car
					new Car(10);
					Car babyCar;
					Car dadCar = population.get(d);
					Car momCar = population.get(m);

					babyCar = dadCar.breed(momCar);

					//population.remove(dadCar);
					//population.remove(momCar);

					population.add(babyCar);
/*
					System.out.println("Dad Ball_x: "+ Arrays.toString(dadCar.balls_x));
					System.out.println("Dad Ball_y: "+ Arrays.toString(dadCar.balls_y));
					System.out.println("Mom Ball_x: "+ Arrays.toString(momCar.balls_x));
					System.out.println("Mom Ball_y: "+ Arrays.toString(momCar.balls_y));
					System.out.println("Baby Ball_x: "+ Arrays.toString(babyCar.balls_x));
					System.out.println("Baby Ball_y: "+ Arrays.toString(babyCar.balls_y));
					System.out.println();
*/
					// Q: What constitutes a "valid" car?
					// check to see if it's a valid car,
/*			System.out.println();
					for (int i=0; i< dadCar.linkmatrix.length; i++) {
						for (int j=0; j<dadCar.linkmatrix.length; j++) {
							System.out.println("dadCar, value at: "+i+" "+j+"  = "+dadCar.linkmatrix[i][j]);
						}
					}
					System.out.println();

					for (int i=0; i< momCar.linkmatrix.length; i++) {
						for (int j=0; j<momCar.linkmatrix.length; j++) {
							System.out.println("momCar, value at: "+i+" "+j+"  = "+momCar.linkmatrix[i][j]);
						}
					}
					System.out.println();

					for (int i=0; i< babyCar.linkmatrix.length; i++) {
						for (int j=0; j<babyCar.linkmatrix.length; j++) {
							System.out.println("babyCar, value at: "+i+" "+j+"  = "+babyCar.linkmatrix[i][j]);
						}
					}
					System.out.println();


 */
					//System.out.println(babyCar.world.);
					/*
					System.out.println("Dad Link Matrix: "+Arrays.deepToString(dadCar.linkmatrix));
					System.out.println();
					System.out.println("Mom Link Matrix: "+Arrays.deepToString(momCar.linkmatrix));
					System.out.println();
					System.out.println("Baby Link Matrix: "+Arrays.deepToString(babyCar.linkmatrix));
					System.out.println();
					System.out.println();

					 */
				//	if (babyCar.getPosition() > 0) {
					bredCars.add(babyCar);
				//	}
				//	}
					// if so, add to our population
					///populationSize++;

				}
			}
		}

		//finally copy the cars in new car over to the population
		/*
		for (int i = 0; i < bredCars.size(); i++) {
			population.add(bredCars.get(i));
		}

		 */

		population.addAll(bredCars);
	}


	//TODO
	//YOU WRITE THIS
	public void mutate()
	{
		//Make an arraylist of new cars
		ArrayList<Car> mutantCars = new ArrayList<>();

		// Go through every car in the population
		for(int i = 0; i < population.size(); i++) {

			//   with probability MUTATE_SELECTION_RATE,
			if (Math.random() < MUTATE_SELECTION_RATE) {

				// call the "mutate" method in class Car and add the child to the new car arraylist
				Car carMutant = population.get(i).mutate(MUTATE_RATE);
				mutantCars.add(carMutant);
				//population.remove(population.get(i));
			}
		}

		population.addAll(mutantCars);
		//finally copy the cars in new car over to the population
	}

	//TODO
	//YOU WRITE THIS
	public void kill()
	{
		//make a "keep" arraylist of cars
		ArrayList<Car> survivorCars = new ArrayList<>();
		Car temp = null;
		boolean compared;
		int initialPopulation = population.size();


		//Do this KILLTOPOPULATION times:
		for (int i = 0; i < KILLTOPOPULATION; i++) {

			temp = population.get(0);
		//	lastHighIndex = 0;

			// go through your population and find the best car.  Use the compare function (below).
			for (int j = 1; j < initialPopulation-i; j++) {
				//if (population.get(i).score_position > population.get(j).score_position) {

				compared = compare(temp, population.get(j));
				//System.out.println("is car A bigger than car B?:  "+compared);

				// if Car j is bigger than Car i...
				if (compared) {
					temp = population.get(j);
			//		lastHighIndex = j;
				//	System.out.println("temp population winner's position: "+temp.score_position);
				}
					//}
		//		System.out.println("winner's position: "+temp.score_position);
			//	counter++;
			}
			//best = new Car(10);
		//	best = temp;
			// remove the best car from population and put it in the keep list
//		    System.out.println(population.get(lastHighIndex).score_position);
			population.remove(temp);
	//		System.out.println(population.get(lastHighIndex).score_position);

			// add best one in the group to our keepArray
			survivorCars.add(temp);

		}


		/*for (int k = 0; k < survivorCars.size(); k++) {
			System.out.println("Survivor car in position "+k+" = "+survivorCars.get(k).score_position);
		}*/
		/*System.out.println(

		);*/
		
		//set population=keep to make the keep list your population
		population = survivorCars;


	}

	//false if a is better, true if b is better
	//Use this in your kill function to select the best cars
	private boolean compare(Car a, Car b)
	{
		if(a.score_position>=500 && b.score_position>=500)
			return b.score_iterations<a.score_iterations;
		else
			return b.score_position>a.score_position;
	}

	//go through every car and race it
	public void raceAll()
	{

		/*for (int i = 0; i < population.size(); i++) {
			System.out.println("BEFORE RACING, position: "+i+" : "+population.get(i).score_position);
		}
		System.out.println();*/

		for(Car car: population) {
			race(car);
		}

		/*for (int i = 0; i < population.size(); i++) {
			System.out.println("AFTER RACING, position: "+i+" : "+population.get(i).score_position);
		}
		System.out.println();*/

	}

	//make a World object containing a racetrack of walls
	// if you do the optional step, you should make several of these and return one of them at random
	public World makeRaceCourse()
	{
		World world = new World();

		if (courseSelected == 0) {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 132, 123, 285);
			world.makeWall(104, 285, 203, 277);
			world.makeWall(202, 275, 271, 344);
			world.makeWall(271, 344, 320, 344);
			world.makeWall(321, 345, 354, 318);
			world.makeWall(354, 318, 394, 324);
			world.makeWall(394, 324, 429, 390);
			world.makeWall(429, 391, 498, 401);

		} else if (courseSelected == 1) {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 94, 53, 279);
			world.makeWall(53, 279, 110, 371);
			world.makeWall(110, 371, 150, 400);
			world.makeWall(150, 400, 195, 409);
			world.makeWall(195, 409, 237, 401);
			world.makeWall(237, 401, 270, 380);
			world.makeWall(270, 380, 307, 364);
			world.makeWall(307, 364, 397, 366);
			world.makeWall(397, 366, 410, 398);
			world.makeWall(410, 398, 400, 423);
			world.makeWall(400, 423, 421, 461);
			world.makeWall(421, 461, 470, 484);
			world.makeWall(470, 484, 499, 499);


		} else if (courseSelected == 2) {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 108, 53, 217);
			world.makeWall(53, 217, 142, 291);
			world.makeWall(142, 291, 283, 347);
			world.makeWall(283, 347, 370, 380);
			world.makeWall(370, 380, 488, 427);
			world.makeWall(488, 427, 499, 499);


		} else if (courseSelected == 3) {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 120, 54, 172);
			world.makeWall(54, 172, 142, 188);
			world.makeWall(142, 188, 162, 210);
			world.makeWall(162, 210, 188, 247);
			world.makeWall(188, 247, 196, 269);
			world.makeWall(196, 269, 218, 300);
			world.makeWall(218, 300, 223, 327);
			world.makeWall(223, 327, 241, 340);
			world.makeWall(241, 340, 266, 331);
			world.makeWall(266, 331, 287, 351);
			world.makeWall(287, 351, 303, 378);
			world.makeWall(303, 378, 322, 390);
			world.makeWall(322, 390, 339, 414);
			world.makeWall(339, 414, 400, 438);
			world.makeWall(400, 438, 466, 431);
			world.makeWall(466, 431, 479, 439);
			world.makeWall(479, 439, 499, 499);

		} else if (courseSelected == 4) {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 81, 15, 192);
			world.makeWall(15, 192, 38, 286);
			world.makeWall(38, 286, 67, 346);
			world.makeWall(67, 346, 95, 378);
			world.makeWall(95, 378, 143, 402);
			world.makeWall(143, 402, 199, 411);
			world.makeWall(199, 411, 239, 407);
			world.makeWall(239, 407, 299, 392);
			world.makeWall(299, 392, 355, 367);
			world.makeWall(355, 367, 388, 356);
			world.makeWall(388, 356, 435, 355);
			world.makeWall(435, 355, 464, 364);
			world.makeWall(464, 364, 499, 499);


		} else {

			world.WIDTH = 500;
			world.HEIGHT = 500;
			world.makeWall(1, 500, 499, 500);
			world.makeWall(-20, 132, 123, 285);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
			world.makeWall(0, 0, 0, 0);
		}


		return world;
	}

	//take an individual car, make a racetrack for it and simulate it
		//at the end of the function the car will have a score
	public void race(Car car)
	{

		World w=makeRaceCourse();
		car.constructCar(w);
		//car.setScore(0);
		int i=0;
		for(i=0; i<ITERATIONS; i++) {
			w.doFrame();
			if(car.getPosition()>=500)
				break;
		}
		//car.getPosition();
	//	System.out.println("i: "+i);
		car.setScore(i);
					// maybe not setting the right score?
	}


	//returns a random integer between [a,b]
	private int randint(int a, int b)
	{
		return (int)(Math.random()*(b-a+1)+a);
	}

	//show every car in population racing, one at a time
	public void showAll()
	{
		for(Car car: population)
		{
			World w=makeRaceCourse();
			car.constructCar(w);
			show(w);
		}
	}

	//show a single car racing
	public void show(Car car)
	{
		World w=makeRaceCourse();
		car.constructCar(w);
		show(w);
	}

	//pop up a window and show a car falling down its track
	private void show(World world)
	{
		JFrame window=new JFrame("World");
		window.setSize(600,600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(world);
		world.addMouseListener(this);
		world.graphics=true;

		window.setVisible(true);

		for(int i=0; i<ITERATIONS; i++)
		{
			world.doFrame();
			try{ Thread.sleep((int)(world.DT*1000/30)); } catch(InterruptedException e){};
		}
	}

	//these methods don't do anything currently
	// they're here only if you want to make the "show" window interactive
	public void mouseClicked(MouseEvent e)
	{
		int px=e.getX();
		int py=e.getY();
	}
	int px,py;
	public void mousePressed(MouseEvent e)
	{
		px=e.getX();
		py=e.getY();
	}
	int rx,ry;
	public void mouseReleased(MouseEvent e)
	{
		rx=e.getX();
		ry=e.getY();
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}


	//main just calls GeneticCars
	public static void main(String[] args)
	{
		new GeneticCars();
	}
}
