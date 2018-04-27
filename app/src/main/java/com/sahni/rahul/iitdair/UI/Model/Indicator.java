package com.sahni.rahul.iitdair.UI.Model;

public class Indicator{

        private float value;

        public Indicator(float value) {
            this.value = value;

        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getValue() {
            return value;
        }

        public float valueToAngle(float value){
            if(value == 0){
                return 135f;
            }
            return 135 + value * (271/501);
        }

        public float valueToAngle(){
            if(value <= 0){
                return 135;
            } else if(value >= 500){
                return 405;
            }
            return (135 + value * (271f/501f));
        }
}
