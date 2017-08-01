
package baseDatos;




public class Tareas {
    private final String tabla = "Jugador";
    public boolean guardarPersona(Connection conexion, Jugador per) throws SQLException, FileNotFoundException{
        boolean verdad = true;
        FileInputStream f = null;        
        try{
            PreparedStatement consulta = null;   
            File file = new File(per.getFoto());
            f = new FileInputStream(file);
            consulta = conexion.prepareStatement("INSERT INTO Jugador (nick, nivel, foto) " + "VALUES(?, ?, ?)");
            consulta.setString(1, per.getNick());
            consulta.setInt(2, per.getNivel()); 
            consulta.setBinaryStream(3, f, (int)file.length());
            consulta.executeUpdate();
            consulta.close();
        }catch(Exception ex){
            verdad = false;
            System.out.println("Error al guardar");            
        }
        return verdad;        
    }
    
    public Jugador recuperarJug(Connection conexion, String nom) throws SQLException {
      Jugador per = new Jugador();
      try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT nick, nivel, foto FROM Jugador WHERE nick = '"+nom+"'");
            ResultSet resultado = consulta.executeQuery();
         while(resultado.next()){           
            per.setNick(resultado.getString("nick"));
            per.setNivel(resultado.getInt("nivel"));
            Blob blob = resultado.getBlob("foto");
            byte[] data = blob.getBytes(1, (int)blob.length());
            BufferedImage img = null;
             try {
                 img = ImageIO.read(new ByteArrayInputStream(data));
             } catch (IOException ex) {
                 Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
             }
             
             per.setIma(img);
         }
          
      }catch(SQLException ex){
         throw new SQLException(ex);
      }
      return per;
   }
    
   public boolean actualizarJugador(Connection conexion, Jugador per, String nombre) throws SQLException, FileNotFoundException{
       boolean verdad = true;
       FileInputStream f = null;  
       try{
            PreparedStatement consulta = null;  
            if(per.getFoto()!=null){
                File file = new File(per.getFoto());    
                f = new FileInputStream(file);
                consulta = conexion.prepareStatement("UPDATE Jugador SET nick = ?,foto = ? WHERE nick = '"+nombre+"'");
                consulta.setString(1, per.getNick()); 
                consulta.setBinaryStream(2, f, (int)file.length());
            }else{                
                consulta = conexion.prepareStatement("UPDATE Jugador SET nick = ? WHERE nick = '"+nombre+"'");
                consulta.setString(1, per.getNick());                
            }
            consulta.executeUpdate();
            consulta.close();
        }catch(Exception ex){
            System.out.println("Error al guardar");
            verdad = false;
      }
       return verdad;
    }
    
   
   
   public void eliminarJugador(Connection conexion, String nombre) throws SQLException{
       try{
         PreparedStatement consulta = conexion.prepareStatement("DELETE FROM Jugador WHERE nick = '"+nombre+"'");
         consulta.executeUpdate();
       }catch(SQLException ex){
         throw new SQLException(ex);
      }
   }
   
   public Vector recuperarJugadores(Connection conexion) throws SQLException, IOException{
      Vector jugadores = new Vector();
      try{
         PreparedStatement consulta = conexion.prepareStatement("SELECT nick, nivel, foto FROM " + this.tabla + " ORDER BY nivel");
         ResultSet resultado = consulta.executeQuery();
         while(resultado.next()){
            Jugador jug = new Jugador();
            jug.setNick(resultado.getString("nick"));
            jug.setNivel(resultado.getInt("nivel"));
            Blob blob = resultado.getBlob("foto");
            byte[] data = blob.getBytes(1, (int)blob.length());
            BufferedImage img = null;
             try {
                 img = ImageIO.read(new ByteArrayInputStream(data));
             } catch (IOException ex) {
                 Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
             }
             
             jug.setIma(img);
             jugadores.add(jug);
            
         }
      }catch(SQLException ex){
         throw new SQLException(ex);
      }
      return jugadores;
   }
}   
   
   
   