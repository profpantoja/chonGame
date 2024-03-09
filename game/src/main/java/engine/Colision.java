package engine;
import java.util.ArrayList;
public class Colision {

	private boolean key;
	
	public boolean listColision(Agent x, ArrayList<Agent> lsAgt) {
			
		for (int w = 0; w < lsAgt.size(); w++) {
			Agent y = (Agent) lsAgt.get(w);
			if (y.isVisible()) {
				
				if (x.getP1() + x.getWidth() >= y.getP1()){} // barreira esquerda
				if (x.getP1() <= y.getP1() + y.getWidth()){} // barreira direita
				if(x.getP2() + x.getHeight() >= y.getP2()){} // barreira cima
				if(x.getP2() <= y.getP2() + y.getHeight()){} // barreira baixo

					//.........Bullet e Enemy Colision...........
					
					if(key){
						if(y.getClass().getName() == "Enemy") {
							y.die();
							lsAgt.remove(w);
							x.die();
//						System.out.println("[ghost]: Oh não! Morri mesmo já morto!");	
						}
					
						if(y.getClass().getName() == "Box") {
							if(x.getClass().getName() == "Bullet" || x.getClass().getName() == "Bullet2"){
								x.die();
								System.out.println("morri");
							}
						}
					
						if (x.direction == Agent.Direction.DOWN ) {
							x.jumpDone = true;
							x.jump = false;
						}
				
				   //..............Beta Colision.................
								
					return true;
					}
				} 
			}
		
		return false;
		}
}	