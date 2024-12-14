import java.awt.Image;
import java.util.Iterator;

class PacMan$Block {
   int x;
   int y;
   int width;
   int height;
   Image image;
   int startX;
   int startY;
   char direction;
   int velocityX;
   int velocityY;

   PacMan$Block(final PacMan var1, Image var2, int var3, int var4, int var5, int var6) {
      this.this$0 = var1;
      this.direction = 'U';
      this.velocityX = 0;
      this.velocityY = 0;
      this.image = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.startX = var3;
      this.startY = var4;
   }

   void updateDirection(char var1) {
      char var2 = this.direction;
      this.direction = var1;
      this.updateVelocity();
      this.x += this.velocityX;
      this.y += this.velocityY;
      Iterator var3 = this.this$0.walls.iterator();

      while(var3.hasNext()) {
         PacMan$Block var4 = (PacMan$Block)var3.next();
         if (this.this$0.collision(this, var4)) {
            this.x -= this.velocityX;
            this.y -= this.velocityY;
            this.direction = var2;
            this.updateVelocity();
         }
      }

   }

   void updateVelocity() {
      if (this.direction == 'U') {
         this.velocityX = 0;
         this.velocityY = -this.this$0.tileSize / 4;
      } else if (this.direction == 'D') {
         this.velocityX = 0;
         this.velocityY = this.this$0.tileSize / 4;
      } else if (this.direction == 'L') {
         this.velocityX = -this.this$0.tileSize / 4;
         this.velocityY = 0;
      } else if (this.direction == 'R') {
         this.velocityX = this.this$0.tileSize / 4;
         this.velocityY = 0;
      }

   }

   void reset() {
      this.x = this.startX;
      this.y = this.startY;
   }
}
