package tutorial;

import java.util.List;

public interface CarService {

	/**
	 * Retrieve all cars in the catalog.
	 * @return all cars
	 */
	public List<Car> findAll();
	
	/**
	 * search cars according to keyword in model and make.
	 * @param keyword for search
	 * @return list of car that match the keyword
	 */
	public List<Car> search(String keyword);

	/**
	 * create and save a new car in the Model.
	 * @param car to create
	 */
	public void saveCar(Car car);

	/**
	 * remove car from the Model.
	 * @param car to remove
	 */
	public void deleteCar(Car car);


}
