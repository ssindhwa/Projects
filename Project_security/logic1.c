
int uprobe___cxa_finalize(struct pt_regs *ctx) {
    int my_id = 204; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe___cxa_finalize(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe___isoc99_scanf(struct pt_regs *ctx) {
    int my_id = 1132; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe___isoc99_scanf(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe___libc_start_main(struct pt_regs *ctx) {
    int my_id = 1298; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe___libc_start_main(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_brk(struct pt_regs *ctx) {
    int my_id = 2569; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_brk(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_connect(struct pt_regs *ctx) {
    int my_id = 10; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_connect(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_dup(struct pt_regs *ctx) {
    int my_id = 12; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_dup(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_getpid(struct pt_regs *ctx) {
    int my_id = 745; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_getpid(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_pipe(struct pt_regs *ctx) {
    int my_id = 2570; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_pipe(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_printf(struct pt_regs *ctx) {
    int my_id = 1897; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_printf(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_read(struct pt_regs *ctx) {
    int my_id = 9; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_read(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_socket(struct pt_regs *ctx) {
    int my_id = 2460; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_socket(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_syscall(struct pt_regs *ctx) {
    int my_id = 2626; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_syscall(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_uname(struct pt_regs *ctx) {
    int my_id = 7; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_uname(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}


int uprobe_write(struct pt_regs *ctx) {
    int my_id = 8; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }
    return 0;
}


int uretprobe_write(struct pt_regs *ctx) {

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}

