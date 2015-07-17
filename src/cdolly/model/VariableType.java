package cdolly.model;

public enum VariableType{
	bool{
		public String toString(){
			return "bool";
		}
	},
	
	pointer{
		public String toString(){
			return "Pointer";
		}
	},

	longint{
		public String toString(){
			return "long int";
		}
	},
	
	uint{
		public String toString(){
			return "unsigned int";
		}
	},
	
	size_t{
		public String toString(){
			return "size_t";
		}			
	},

	intt{
		public String toString(){
			return "int";
		}
	},
	
	floatt{
		public String toString(){
			return "float";
		}
	},
	
	doublee{
		public String toString(){
			return "double";
		}
	},
	
	longg{
		public String toString(){
			return "long";
		}
	},
	
	ulong{
		public String toString(){
			return "unsigned long";
		}
	},
	
	ushort{
		public String toString(){
			return "unsigned short";
		}
	},

	shortt{
		public String toString(){
			return "short";
		}
	},

	ucharr{
		public String toString(){
			return "unsigned char";
		}
	},
	
	charr{
		public String toString(){
			return "char";
		}
	},
	
	string{
		public String toString(){
			return "char*";
		}
	},

	
	structt{
		public String toString(){
			return "struct";
		}
	}, 
	
	unionn{
		public String toString(){
			return "union";
		}
	}, 
	
	voidd{
		public String toString(){
			return "void";
		}
	},
	
	genericPointer{
		public String toString(){
			return "void*";
		}
	}, 

	
	enumm{
		public String toString(){
			return "enum";
		}
	}, 
	int8{
		public String toString(){
			return "int8_t";
		}
	}, 
	int16{
		public String toString(){
			return "int16_t";
		}
	},
	int32{
		public String toString(){
			return "int32_t";
		}
	},
	int64{
		public String toString(){
			return "int64_t";
		}
	},
	uint8{
		public String toString(){
			return "uint8_t";
		}
	},
	uint16{
		public String toString(){
			return "uint16_t";
		}
	},
	uint32{
		public String toString(){
			return "uint32_t";
		}
	},
	uint64{
		public String toString(){
			return "uint64_t";
		}
	},
	file{
		public String toString(){
			return "FILE*";
		}
	}

	; 

}

