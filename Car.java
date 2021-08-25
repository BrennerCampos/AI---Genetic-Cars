/*
MODIFY THIS CLASS TO MATE TWO CARS AND MUTATE CARS

Your code will go in methods BREED() and MUTATE().  Find the TODO lines.
  you will call these methods from your code in GeneticCars

A "Car" is a collection of balls and links
*/

public class Car
{
	//how many balls in the car
	int nodes;
	//position of balls
	int[] balls_x;
	int[] balls_y;
	//for every ball i,j  true if there's a link between them
	boolean[][] linkmatrix;

	//these are set by the setScore function after a simulated race
	double score_position;		//how far did the car get
	double score_iterations;	//how long did it take the car to reach the end

	//the simulated world the car is running in.  null until the car is raced.
	World world;

	//construct a car with nodes balls and random links
	//every ball is placed between (5,5) and (50,50)

	public Car(int nodes)
	{
		this.world=null;
		this.nodes=nodes;

		balls_x=new int[nodes];
		balls_y=new int[nodes];
		linkmatrix=new boolean[nodes][nodes];

		//randomly place balls between (5,5 and 50,50)
		for(int i=0; i<nodes; i++)
		{
			balls_x[i]=randint(5,50);
			balls_y[i]=randint(5,50);
		}

		//assign a link between two balls with probability 1/3
		for(int i=0; i<nodes; i++)
		{
			for(int j=0; j<nodes; j++)
			{
				if(randint(1,3)==1)
					linkmatrix[i][j]=true;
			}
		}
	}

	//return the average x position of the nodes
	//this is called only after the car has been raced
	public double getPosition()
	{
		int sum=0;
		for(int i=0; i<nodes; i++)
			sum+=world.getBall(i).position.x;
		return sum/nodes;
	}

	//set the car's score
	//this is called once the race simulation is done
		//don't call it before then or you'll get a nullpointerexception
	public void setScore(int iterations)
	{
		score_position=getPosition();
		if(score_position>world.WIDTH)
			score_position=world.WIDTH;
		score_iterations=iterations;
	}

	//build the car into the world: create its balls and links
	//call this when you're ready to start racing
	public void constructCar(World world)
	{
		this.world=world;
		for(int i=0; i<nodes; i++)
		{
			world.makeBall(balls_x[i],balls_y[i]);
		}
		for(int i=0; i<nodes; i++)
			for(int j=0; j<nodes; j++)
				if(linkmatrix[i][j])
					world.makeLink(i,j);
	}

	//returns a random integer between [a,b]
	private int randint(int a, int b)
	{
		return (int)(Math.random()*(b-a+1)+a);
	}

	//returns a random double between [a,b]
	private double randdouble(double a, double b)
	{
		//System.out.println(Math.random()*(b-a+1)+a);
		return (Math.random()*(b-a+1)+a);
	}


	//YOU WRITE THIS FUNCTION
	//It should return a "child" car that is the crossover between this car and parameter car c
	public Car breed(Car c)
	{
		Car child=new Car(nodes);
	//	System.out.println("Nodes: "+nodes);



		//YOUR WORK HERE

		//Choose a random crossover point.  Also choose a car to go first
		int carCrossover = randint(0, nodes);

		//System.out.println("carCrossover: "+carCrossover);
		// ---- BALLS -------------------------
		// this car goes first (dad)
		// copy the balls from the first car's balls_x and balls_y to the child
		for (int i=0; i<carCrossover; i++) {
			child.balls_x[i] = this.balls_x[i];
			child.balls_y[i] = this.balls_y[i];
		}
		// then parameter car (mom)
		// after the crossover, copy the balls_x and balls_y from the second car to the child
		for (int j=carCrossover; j<nodes; j++) {
			//System.out.println(nodes);
			child.balls_x[j] = c.balls_x[j];
			child.balls_y[j] = c.balls_y[j];
		}

		// ---- LINKS ---------------------------
		//pick a new crossover point, then do the same with the linkmatrix
		int linkCrossover = randint(0, linkmatrix.length);


		//System.out.println("linkCrossover: "+linkCrossover);
		// dad link matrix copy
		for (int i=0; i<linkmatrix.length; i++) {
			for (int j=0; j<linkCrossover; j++) {
				child.linkmatrix[i][j] = this.linkmatrix[i][j];
			}
		}

		// mom link matrix copy
		for (int i= 0; i<linkmatrix.length; i++) {
			for (int j=linkCrossover; j<nodes; j++) {
				child.linkmatrix[i][j] = c.linkmatrix[i][j];
			}
		}

		// at this point, child should be a genetic offspring of mom and dad (this car and taken parameter car)
	//	System.out.println("Child: "+child.linkmatrix);
	//	System.out.println("Mom: "+c.linkmatrix);
	//	System.out.println("Dad: "+this.linkmatrix);
		return child;
	}

	//TODO
	//YOU WRITE THIS FUNCTION
	//It should return a car "newcar" that is identical to the current car, except with mutations
	public Car mutate(double probability)
	{
		Car newcar=new Car(nodes);
		double randRoll = 0.0;

		//YOUR WORK HERE
		//  You should copy over the car's balls_x and balls_y to newcar
		for (int i=0; i<nodes; i++) {
			newcar.balls_x[i] = this.balls_x[i];
			newcar.balls_y[i] = this.balls_y[i];


			//with probability "probability", change the balls_x and balls_y to a random number from 5 to 50
			randRoll = randdouble(0.0, 1.0);

		//	System.out.println(randRoll);

			if (randRoll<probability){
	//			System.out.println("got one!");
				for(int j=0; j<nodes; j++)
				{
				//	System.out.println("mutating at: "+j);
					newcar.balls_x[j]=randint(5,50);
					newcar.balls_y[j]=randint(5,50);
				}
			}


			//  Then copy over the links
			for (int m=0; m< linkmatrix.length; m++) {
				for (int n=0; n< linkmatrix.length; n++) {
					newcar.linkmatrix[m][n] = this.linkmatrix[m][n];
				}
			}


			randRoll = randdouble(0, 1);
		//	System.out.println("randRoll for links: "+randRoll);

			//	with probability "probability",
			if (randRoll<probability){
			//	System.out.println("changing links");

				for (int x=0; x< linkmatrix.length; x++) {
					for (int y=0; y<linkmatrix.length; y++) {

						boolean changeLink = false;

						// set the link to true/false (50/50 chance)
						if (randint(0, 1)==1) {
					//		System.out.println("Changing links at: x: "+x+"  y: "+ y);
							changeLink = true;
						}
						//System.out.println("before change: "+newcar.linkmatrix[x][y]);

						if (changeLink) {
							newcar.linkmatrix[x][y] = !newcar.linkmatrix[x][y];
				//			System.out.println("after change: "+newcar.linkmatrix[x][y]);
						}
					}
				}
			}
		}
		return newcar;
	}
}
