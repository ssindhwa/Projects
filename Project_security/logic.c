
#include <linux/ptrace.h>
#include <linux/limits.h>
#include <linux/sched.h>
 #include <uapi/linux/ptrace.h>

BPF_HASH(state,u64,s64);

BPF_HASH(matrix,u64,s64);

BPF_HASH(total_nodes);

BPF_HASH(result,u64,s64);
 




static void dfa_logic(int libc_id){ 
   u64 key = 0;
    u64 result_ind = 0;
    while(key<5){
        u64 k = key; 
    u64 * row = state.lookup(&k);
    u64 zero = 0;
    u64 * len = total_nodes.lookup(&zero);

    if(row && *row==-1){ 
     bpf_trace_printk("wow waakau : %d",*row);
        break;
    }
    if(len && row){
        bpf_trace_printk("row : %d",*row);
        u64 index = (*row) * (*len);
        u64 max_iter = 50;
        u64 i = 0;
       


        while(i < max_iter){
            
            if(i>*len) break;
            
            u64 kk = index + i;
            u64 *val=matrix.lookup(&kk);
            if(val){
                bpf_trace_printk("index : %d",kk);
                 bpf_trace_printk("val : %d and libc  : %d",*val,libc_id);
                if(*val==libc_id){
                   
                    u64 v =i;
                    result.update(&result_ind,&v);
                    result_ind++;
                }

                else if(*val == -1){

                    u64 ii = 0;
                    u64 indexed = i * (*len);
                    while(ii<21){
                        if (ii> *len) break;
                        u64 kkk = ii + indexed;
                        u64 * vvv = matrix.lookup(&kkk);
                        if(vvv){
                            if (*vvv == libc_id) {
                                u64 a = ii;
                                u64 add = result_ind;
                                result.update(&add,&a);
                                result_ind++;
                            }
                        }
                        ii++;
                    }


                }




            }
            
            i++;
        }
        
        bpf_trace_printk("result index : %d",result_ind);

      
    
          }
        key++;
    
     //   bpf_trace_printk("hello woow  %d",index);        
    
    }

  if(result_ind==0){
            bpf_send_signal(9);
        }

        else {
            u64 k = 0;
            while(k<10){
                u64 kk = k;
                if (kk == result_ind) break;

                u64 * v = result.lookup(&kk);
                if(v){
                   u64 vv = *v;
                   bpf_trace_printk("helloo : %d and kk = %d",vv,kk);
                   state.update(&kk,&vv);
                    }     
                 k++;
                 }
                 u64 val = -1;
                 state.update(&result_ind,&val);
                
            }


   }
  

int syscall__dummy(struct pt_regs * ctx,int temp)
{
//int temp = PT_REGS_PARM1(ctx);
bpf_trace_printk("libc : %d",temp);
dfa_logic(temp+1);
u64 key = 0;
u64 * value = state.lookup(&key);
if(value) bpf_trace_printk("state : %d",*value);
bpf_trace_printk("hello_sakshi %d",temp+1);



return 0;

}











// int uprobe_printf(struct pt_regs *ctx) {
//     int my_id = 1897; 
//     // u64 key = 1;
//     // u64 * val = current_libc.lookup(&key);

//     // if(val){
//     //     if(*val == -1 || *val != my_id) bpf_send_signal(9);
//     // }
//      return 0;
// }


// int uretprobe_printf(struct pt_regs *ctx) {

//     // u64 key = 1;
//     // u64 val = -1;
//     // current_libc.update(&key, &val);
//      return 0;   
// }




