#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
//const int maxn = 100010;
#define maxn 1000010
char s_t[maxn], s[maxn];
int i, l, sign_num, sign_poly, num, coff_num, power_num;
int poly[maxn];
int myisnumber(char c) {
	if (c >= '0'&&c <= '9') {
		return 1;
	}
	else {
		return 0;
	}
}

int main(int argc, const char * argv[]) {
	
	memset(poly, 0, sizeof(poly));

	gets(s_t);
	//l = strlen(s);
	l = 0;
	for (i = 0; i < strlen(s_t); ++i) {
		if (!isspace(s_t[i])) {
			s[l++] = s_t[i];
		}
	}
	if (l == 0) {
		printf("Invalid input\n");
		return 0;
	}
	int cnt_poly, cnt_num;
	cnt_poly = cnt_num = 0;
	for (i = 0; i < l; ++i) {
		cnt_poly += (s[i] == '{');
		cnt_num += (s[i] == '(');
	}
	if (cnt_poly > 20){ //|| cnt_num > 50) {
		printf("Invalid input\n");
		return 0;
	}
	i = 0;
	while (i < l) {
		while (isspace(s[i]) || s[i] == '}')
			++i;
		if (i == l) {
			break;
		}
		if (s[i] != '{'  && s[i] != '+' && s[i] != '-') {
			printf("Invalid input\n");
			return 0;
		}
		sign_poly = 1;
		if (s[i] == '+') {
			++i;
			while (isspace(s[i]))
				++i;
		}
		else
			if (s[i] == '-') {
				sign_poly = -1;
				++i;
				while (isspace(s[i]))
					++i;
			}

		if (s[i] != '{') {
			//printf("EXCEPTION");
			printf("Invalid input\n");
			return 0;
		}
		else {//s[i]=='{'
			++i;
			if (s[i] == '}') {
				//printf("Exception: {} is not permitted");
				printf("Invalid input\n");
				return 0;
			}
		}
		while (s[i] != '}') {
			//compute the cofficient
			while (isspace(s[i]) || s[i] == '(' || s[i] == ',') {
				++i;
			}
			sign_num = 1;
			if (s[i] == '+') {
				++i;
			}
			else
				if (s[i] == '-') {
					sign_num = -1;
					++i;
				}
				else {
					if (!myisnumber(s[i])) {
						//printf("Invalid Input:Lack of cofficent");
						printf("Invalid input\n");
						return 0;
					}
				}

				num = 0;
				while (myisnumber(s[i])) {
					num = num * 10 + s[i] - '0';
					++i;
				}
				coff_num = sign_num * num;

				while (isspace(s[i]) || s[i] == ',') {
					++i;
				}
				sign_num = 1;
				if (s[i] == '+') {
					++i;
				}
				else
					if (s[i] == '-') {
						sign_num = -1;
						++i;
					}
					else {
						if (!myisnumber(s[i])) {
							//printf("Invalid Input:Lack of power");
							printf("Invalid input\n");
							return 0;
						}
					}
					num = 0;
					while (myisnumber(s[i])) {
						num = num * 10 + s[i] - '0';
						++i;
					}
					power_num = sign_num*num;
					if (power_num < 0){
						printf("Invalid input\n");
						return 0;
					} 
					poly[power_num] += coff_num*sign_poly;

					while (isspace(s[i]) || s[i] == ')') {
						++i;
					}//until the char is ','||'}'
		}
	}


	/*for (i = 0; i < maxn; ++i){
	if (poly[i] != 0){
	if (tot!=0){
	if (poly[i]>=0)
	printf("+ ");
	else{
	printf("- ");
	}
	}
	printf("%dx^%d ",abs(poly[i]),i);
	tot++;
	}
	}*/
	int tot = 0;
	int f_zero = 1;
	for (i = 0; i < maxn; ++i) {
		if (poly[i] != 0) {
			f_zero = 0;
			if (tot == 0) {
				printf("{(%d,%d)", poly[i], i);
			}
			else {
				printf(", (%d,%d)", poly[i], i);
			}
			tot++;
		}
	}
	if (f_zero) {
		printf("{(0,0)}\n");
	}
	else {
		printf("}\n");
	}
	return 0;
}
// In: {(3,0), (2,2), (12,3)} + {(3,1), (-5,3)} - {(-199,2), (29,3),(10,7)}
// Out:{(3,0), (3,1), (201,2), (-22,3), (-10,7)}
