package co.edu.eam.lugaresapp.model

/**
 * DATOS DE UBICACIÓN: DEPARTAMENTOS Y CIUDADES DE COLOMBIA
 * 
 * Este objeto singleton contiene la estructura jerárquica de departamentos y ciudades.
 * 
 * CONCEPTOS CLAVE:
 * - object: Singleton en Kotlin (única instancia en toda la app)
 * - mapOf: Crea un Map inmutable (no se puede modificar después de creado)
 * - Map<String, List<String>>: Estructura clave-valor
 *   * Clave: Nombre del departamento
 *   * Valor: Lista de ciudades de ese departamento
 * 
 * USO:
 * - Para obtener departamentos: LocationData.departmentsAndCities.keys.toList()
 * - Para obtener ciudades de un departamento: LocationData.departmentsAndCities["Antioquia"]
 * 
 * VENTAJAS:
 * - Datos centralizados (única fuente de verdad)
 * - Fácil actualización y mantenimiento
 * - Type-safe (seguridad de tipos)
 */
object LocationData {
    
    /**
     * Mapa de departamentos y sus ciudades principales
     * Estructura: Map<Departamento, List<Ciudades>>
     */
    val departmentsAndCities = mapOf(
        "Amazonas" to listOf(
            "Leticia", "Puerto Nariño", "El Encanto", "La Chorrera", "La Pedrera", 
            "La Victoria", "Miriti-Paraná", "Puerto Alegría", "Puerto Arica", 
            "Puerto Santander", "Tarapacá"
        ),
        "Antioquia" to listOf(
            "Medellín", "Bello", "Itagüí", "Envigado", "Apartadó", "Turbo", 
            "Rionegro", "Caldas", "La Estrella", "Sabaneta", "Copacabana", 
            "Girardota", "Barbosa", "Carmen de Viboral", "El Retiro", "Guarne", 
            "La Ceja", "Marinilla", "San Pedro de los Milagros", "Santa Rosa de Osos"
        ),
        "Arauca" to listOf(
            "Arauca", "Arauquita", "Cravo Norte", "Fortul", "Puerto Rondón", 
            "Saravena", "Tame"
        ),
        "Atlántico" to listOf(
            "Barranquilla", "Soledad", "Malambo", "Sabanalarga", "Puerto Colombia", 
            "Galapa", "Baranoa", "Palmar de Varela", "Santo Tomás", "Usiacurí", 
            "Candelaria", "Luruaco", "Manatí", "Repelón", "Sabanagrande", 
            "Santa Lucía", "Suan", "Tubará", "Campo de la Cruz", "Juan de Acosta", 
            "Piojó", "Polonuevo", "Ponedera"
        ),
        "Bogotá D.C." to listOf(
            "Bogotá"
        ),
        "Bolívar" to listOf(
            "Cartagena", "Magangué", "Turbaco", "Arjona", "El Carmen de Bolívar", 
            "Mompós", "Simití", "Magangué", "Santa Rosa del Sur", "San Jacinto", 
            "San Juan Nepomuceno", "María la Baja", "Mahates", "Clemencia", 
            "Santa Catalina", "Soplaviento", "Turbana", "Villanueva"
        ),
        "Boyacá" to listOf(
            "Tunja", "Duitama", "Sogamoso", "Chiquinquirá", "Paipa", "Villa de Leyva", 
            "Puerto Boyacá", "Moniquirá", "Samacá", "Ventaquemada", "Toca", 
            "Nobsa", "Tibasosa", "Aquitania", "Guateque", "Garagoa", "Miraflores", 
            "Paz de Río", "Socha", "Tasco"
        ),
        "Caldas" to listOf(
            "Manizales", "La Dorada", "Chinchiná", "Villamaría", "Riosucio", 
            "Aguadas", "Anserma", "Aranzazu", "Belalcázar", "Filadelfia", 
            "La Merced", "Manzanares", "Marmato", "Marquetalia", "Marulanda", 
            "Neira", "Norcasia", "Pácora", "Palestina", "Pensilvania", 
            "Risaralda", "Salamina", "Samaná", "San José", "Supía", 
            "Victoria", "Viterbo"
        ),
        "Caquetá" to listOf(
            "Florencia", "San Vicente del Caguán", "Puerto Rico", "El Doncello", 
            "El Paujil", "La Montañita", "Milán", "Morelia", "Solano", 
            "Solita", "Valparaíso", "Cartagena del Chairá", "Curillo", 
            "San José del Fragua", "Albania", "Belén de los Andaquíes"
        ),
        "Casanare" to listOf(
            "Yopal", "Aguazul", "Villanueva", "Tauramena", "Monterrey", 
            "Paz de Ariporo", "Maní", "Trinidad", "Hato Corozal", "Orocué", 
            "Pore", "Sabanalarga", "Nunchía", "Támara", "Recetor", 
            "San Luis de Palenque", "Chámeza", "Sácama", "La Salina"
        ),
        "Cauca" to listOf(
            "Popayán", "Santander de Quilichao", "Puerto Tejada", "Patía", 
            "Piendamó", "Miranda", "Corinto", "Guapi", "Timbío", "El Tambo", 
            "Cajibío", "Caldono", "Caloto", "Jambaló", "La Vega", "Mercaderes", 
            "Morales", "Padilla", "Páez", "Rosas", "Silvia", "Sotará", 
            "Suárez", "Sucre", "Timbiquí", "Toribío", "Totoró"
        ),
        "Cesar" to listOf(
            "Valledupar", "Aguachica", "Bosconia", "La Jagua de Ibirico", 
            "Curumaní", "Chimichagua", "Chiriguaná", "Codazzi", "El Copey", 
            "El Paso", "Gamarra", "González", "La Gloria", "La Paz", 
            "Manaure Balcón del Cesar", "Pailitas", "Pelaya", "Pueblo Bello", 
            "Río de Oro", "San Alberto", "San Diego", "San Martín", 
            "Tamalameque", "Astrea"
        ),
        "Chocó" to listOf(
            "Quibdó", "Istmina", "Condoto", "Tadó", "Acandí", "Alto Baudó", 
            "Atrato", "Bagadó", "Bahía Solano", "Bajo Baudó", "Bojayá", 
            "Cértegui", "El Cantón del San Pablo", "El Carmen de Atrato", 
            "El Carmen del Darién", "El Litoral del San Juan", "Juradó", 
            "Lloró", "Medio Atrato", "Medio Baudó", "Medio San Juan", 
            "Nóvita", "Nuquí", "Río Iró", "Río Quito", "Riosucio", 
            "San José del Palmar", "Sipí", "Unguía", "Unión Panamericana"
        ),
        "Córdoba" to listOf(
            "Montería", "Cereté", "Sahagún", "Lorica", "Planeta Rica", 
            "Montelíbano", "Tierralta", "Ayapel", "San Antero", "San Bernardo del Viento", 
            "Ciénaga de Oro", "Chinú", "Cotorra", "La Apartada", "Los Córdobas", 
            "Momil", "Moñitos", "Pueblo Nuevo", "Puerto Escondido", "Puerto Libertador", 
            "Purísima", "San Andrés Sotavento", "San Carlos", "San José de Uré", 
            "San Pelayo", "Tuchín", "Valencia"
        ),
        "Cundinamarca" to listOf(
            "Soacha", "Girardot", "Fusagasugá", "Facatativá", "Zipaquirá", 
            "Chía", "Mosquera", "Madrid", "Funza", "Cajicá", "Sibaté", 
            "Tocancipá", "La Calera", "Sopó", "Tabio", "Tenjo", "Cota", 
            "Gachancipá", "Bojacá", "El Rosal", "Subachoque", "Nemocón", 
            "Suesca", "Chocontá", "Villeta", "Guaduas", "La Mesa", 
            "Anapoima", "Apulo", "Arbeláez", "Cabrera", "Fusagasugá", 
            "Granada", "Pandi", "Pasca", "San Bernardo", "Silvania", 
            "Tibacuy", "Venecia", "Agua de Dios", "Ricaurte", "Tocaima", 
            "Albán", "La Peña", "La Vega", "Nimaima", "Nocaima", 
            "Quebradanegra", "San Francisco", "Sasaima", "Útica", "Vergara", 
            "Vianí", "Zipacón"
        ),
        "Guainía" to listOf(
            "Inírida", "Barranco Minas", "Mapiripana", "San Felipe", 
            "Puerto Colombia", "La Guadalupe", "Cacahual", "Pana Pana", "Morichal"
        ),
        "Guaviare" to listOf(
            "San José del Guaviare", "Calamar", "El Retorno", "Miraflores"
        ),
        "Huila" to listOf(
            "Neiva", "Pitalito", "Garzón", "La Plata", "Campoalegre", 
            "Algeciras", "Gigante", "Hobo", "Íquira", "Rivera", "Tello", 
            "Teruel", "Aipe", "Baraya", "Colombia", "La Argentina", 
            "Nátaga", "Palermo", "Tesalia", "Acevedo", "Agrado", 
            "Altamira", "Elías", "Guadalupe", "Isnos", "Oporapa", 
            "Palestina", "Pital", "Saladoblanco", "San Agustín", "Suaza", 
            "Tarqui", "Timaná", "Yaguará"
        ),
        "La Guajira" to listOf(
            "Riohacha", "Maicao", "Uribia", "Manaure", "San Juan del Cesar", 
            "Villanueva", "Fonseca", "Barrancas", "Distracción", "Dibulla", 
            "Albania", "Hatonuevo", "La Jagua del Pilar", "El Molino", "Urumita"
        ),
        "Magdalena" to listOf(
            "Santa Marta", "Ciénaga", "Fundación", "Plato", "El Banco", 
            "Zona Bananera", "Aracataca", "Puebloviejo", "Sabanas de San Ángel", 
            "Sitionuevo", "Algarrobo", "Ariguaní", "Cerro San Antonio", 
            "Chivolo", "Concordia", "El Piñón", "El Retén", "Guamal", 
            "Nueva Granada", "Pedraza", "Pijiño del Carmen", "Pivijay", 
            "Remolino", "Salamina", "San Sebastián de Buenavista", "San Zenón", 
            "Santa Ana", "Santa Bárbara de Pinto", "Tenerife", "Zapayán"
        ),
        "Meta" to listOf(
            "Villavicencio", "Acacías", "Granada", "San Martín", "Puerto López", 
            "Cumaral", "Restrepo", "Puerto Gaitán", "Guamal", "Castilla la Nueva", 
            "San Carlos de Guaroa", "Barranca de Upía", "Cabuyaro", "Fuente de Oro", 
            "La Macarena", "Lejanías", "Mapiripán", "Mesetas", "Puerto Concordia", 
            "Puerto Lleras", "Puerto Rico", "Uribe", "Vistahermosa", "El Calvario", 
            "El Castillo", "El Dorado", "San Juan de Arama", "San Juanito"
        ),
        "Nariño" to listOf(
            "Pasto", "Tumaco", "Ipiales", "Túquerres", "Barbacoas", "La Unión", 
            "Sandoná", "Samaniego", "La Cruz", "El Charco", "Cumbal", "Guachucal", 
            "Pupiales", "Aldana", "Ancuyá", "Arboleda", "Barbacoas", "Belén", 
            "Buesaco", "Chachagüí", "Colón", "Consacá", "Contadero", "Córdoba", 
            "Cuaspud", "Cumbitara", "El Peñol", "El Rosario", "El Tablón de Gómez", 
            "El Tambo", "Francisco Pizarro", "Funes", "Iles", "Imués", 
            "La Florida", "La Llanada", "La Tola", "Leiva", "Linares", 
            "Los Andes", "Magüí", "Mallama", "Mosquera", "Nariño", 
            "Olaya Herrera", "Ospina", "Policarpa", "Potosí", "Providencia", 
            "Puerres", "Ricaurte", "Roberto Payán", "San Andrés de Tumaco", 
            "San Bernardo", "San Lorenzo", "San Pablo", "San Pedro de Cartago", 
            "Santacruz", "Sapuyes", "Taminango", "Tangua", "Yacuanquer"
        ),
        "Norte de Santander" to listOf(
            "Cúcuta", "Ocaña", "Pamplona", "Villa del Rosario", "Los Patios", 
            "El Zulia", "San Cayetano", "Tibú", "Sardinata", "El Tarra", 
            "Convención", "Ábrego", "Hacarí", "La Playa", "Teorama", 
            "Arboledas", "Cucutilla", "Gramalote", "Lourdes", "Salazar", 
            "Santiago", "Villa Caro", "Bochalema", "Chinácota", "Durania", 
            "Herrán", "Labateca", "Ragonvalia", "Toledo", "Bucarasica", 
            "Cachirá", "Cáchira", "Chitagá", "Mutiscua", "Pamplonita", 
            "Silos", "Cácota", "Chitagá"
        ),
        "Putumayo" to listOf(
            "Mocoa", "Puerto Asís", "Valle del Guamuez", "Orito", "Puerto Guzmán", 
            "Puerto Leguízamo", "Villagarzón", "Sibundoy", "San Francisco", 
            "Santiago", "Colón", "San Miguel", "Puerto Caicedo"
        ),
        "Quindío" to listOf(
            "Armenia", "Calarcá", "Montenegro", "La Tebaida", "Quimbaya", 
            "Circasia", "Salento", "Filandia", "Génova", "Pijao", "Buenavista", "Córdoba"
        ),
        "Risaralda" to listOf(
            "Pereira", "Dosquebradas", "Santa Rosa de Cabal", "La Virginia", 
            "Marsella", "Belén de Umbría", "Apía", "Balboa", "Guática", 
            "La Celia", "Mistrató", "Pueblo Rico", "Quinchía", "Santuario"
        ),
        "San Andrés y Providencia" to listOf(
            "San Andrés", "Providencia"
        ),
        "Santander" to listOf(
            "Bucaramanga", "Floridablanca", "Girón", "Piedecuesta", "Barrancabermeja", 
            "San Gil", "Socorro", "Barbosa", "Málaga", "Vélez", "Zapatoca", 
            "Lebrija", "Sabana de Torres", "Rionegro", "Cimitarra", "Puerto Wilches", 
            "Charalá", "El Carmen de Chucurí", "San Vicente de Chucurí", "Simacota", 
            "Aguada", "Albania", "Aratoca", "Barichara", "Betulia", 
            "Bolívar", "Cabrera", "California", "Capitanejo", "Carcasí", 
            "Cepitá", "Cerrito", "Chipatá", "Cimitarra", "Concepción", 
            "Confines", "Contratación", "Coromoro", "Curití", "El Guacamayo", 
            "El Peñón", "El Playón", "Encino", "Enciso", "Florián", 
            "Galán", "Gámbita", "Guaca", "Guadalupe", "Guapotá", 
            "Guavatá", "Güepsa", "Hato", "Jesús María", "Jordán", 
            "La Belleza", "Landázuri", "La Paz", "Lebríja", "Los Santos", 
            "Macaravita", "Matanza", "Mogotes", "Molagavita", "Ocamonte", 
            "Oiba", "Onzaga", "Palmar", "Palmas del Socorro", "Páramo", 
            "Piedecuesta", "Pinchote", "Puente Nacional", "Puerto Parra", 
            "San Andrés", "San Benito", "San José de Miranda", "San Miguel", 
            "Santa Bárbara", "Santa Helena del Opón", "Suaita", "Sucre", 
            "Suratá", "Tona", "Valle de San José", "Vetas", "Villanueva"
        ),
        "Sucre" to listOf(
            "Sincelejo", "Corozal", "Sampués", "San Marcos", "Tolú", 
            "Sincé", "Majagual", "Ovejas", "San Onofre", "Coveñas", 
            "Buenavista", "Caimito", "Chalán", "Coloso", "El Roble", 
            "Galeras", "Guaranda", "La Unión", "Los Palmitos", "Morroa", 
            "Palmito", "San Antonio de Palmito", "San Benito Abad", "San Juan de Betulia", 
            "San Luis de Sincé", "San Pedro", "Santiago de Tolú", "Sucre", "Tolú Viejo"
        ),
        "Tolima" to listOf(
            "Ibagué", "Espinal", "Melgar", "Honda", "Líbano", "Chaparral", 
            "Mariquita", "Girardot", "Purificación", "Guamo", "Flandes", 
            "Armero Guayabal", "Fresno", "Cajamarca", "Venadillo", "Rovira", 
            "Planadas", "Ambalema", "Anzoátegui", "Ataco", "Alpujarra", 
            "Alvarado", "Casabianca", "Carmen de Apicalá", "Coello", "Cunday", 
            "Dolores", "Falan", "Herveo", "Icononzo", "Lérida", 
            "Murillo", "Natagaima", "Ortega", "Palocabildo", "Piedras", 
            "Prado", "Roncesvalles", "Saldaña", "San Antonio", "San Luis", 
            "Santa Isabel", "Suárez", "Valle de San Juan", "Villahermosa", "Villarrica"
        ),
        "Valle del Cauca" to listOf(
            "Cali", "Palmira", "Buenaventura", "Tuluá", "Cartago", "Buga", 
            "Jamundí", "Yumbo", "Candelaria", "Florida", "Pradera", "Zarzal", 
            "Sevilla", "Roldanillo", "El Cerrito", "Dagua", "La Unión", "Vijes", 
            "Alcalá", "Andalucía", "Ansermanuevo", "Argelia", "Bolívar", 
            "Bugalagrande", "Caicedonia", "Calima", "El Águila", "El Cairo", 
            "El Dovio", "Ginebra", "Guacarí", "La Cumbre", "La Victoria", 
            "Obando", "Restrepo", "Riofrío", "San Pedro", "Toro", 
            "Trujillo", "Ulloa", "Versalles", "Yotoco"
        ),
        "Vaupés" to listOf(
            "Mitú", "Carurú", "Pacoa", "Taraira", "Papunaua", "Yavaraté"
        ),
        "Vichada" to listOf(
            "Puerto Carreño", "La Primavera", "Santa Rosalía", "Cumaribo"
        )
    )
    
    /**
     * Función helper para obtener lista de departamentos ordenada alfabéticamente
     */
    fun getDepartments(): List<String> {
        return departmentsAndCities.keys.sorted()
    }
    
    /**
     * Función helper para obtener ciudades de un departamento específico
     * @param department Nombre del departamento
     * @return Lista de ciudades o lista vacía si el departamento no existe
     */
    fun getCitiesByDepartment(department: String): List<String> {
        return departmentsAndCities[department] ?: emptyList()
    }
}
