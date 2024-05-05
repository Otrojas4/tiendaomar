
package com.ecom.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.entity.Product;

/**
 * Esta interfaz representa el DAO (Data Access Object) para la entidad Product.
 * Proporciona métodos para acceder y manipular los datos de los productos en la base de datos.
 */
@Repository
public interface ProductDao extends CrudRepository<Product, Integer>{

	/**
	 * Recupera una lista paginada de todos los productos.
	 * 
	 * @param pageable la información de paginación
	 * @return una lista de productos paginada
	 */
	public List<Product> findAll(Pageable pageable);
	
	/**
	 * Busca productos cuyo nombre o descripción contengan las palabras clave especificadas.
	 * La búsqueda es insensible a mayúsculas y minúsculas.
	 * 
	 * @param key1 la primera palabra clave
	 * @param key2 la segunda palabra clave
	 * @param pageable la información de paginación
	 * @return una lista de productos que coinciden con las palabras clave
	 */
	public List<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
			String key1, String key2, Pageable pageable);
}
