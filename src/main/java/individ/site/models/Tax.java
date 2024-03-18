package individ.site.models;
// import java.util.List;
// import java.util.ArrayList;
// import jakarta.persistence.OneToMany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tax implements Deduction{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "percent_rate", nullable = false)
    private Double percentRate;

    @Column(name = "tax_name", nullable = false)
    private String taxName;

    @Column(name = "description", nullable = true)
    private String description;
    

    // @OneToMany(mappedBy = "tax")
    // private List<PayrollTax> payroll_tax = new ArrayList<>();

    // // Constructors, getters, and setters
    
    
    // public void setPayrollTax(PayrollTax payrollTax) {
    //     if (payrollTax.getTax() != null) {
    //         payrollTax.getTax().getPayrollTax().remove(payrollTax);
    //     }
    //     payrollTax.setTax(this);
    //     payroll_tax.add(payrollTax);
    // }

    // public List<PayrollTax> getPayrollTax() {
    //     return payroll_tax;
    // }

    @Override
    public void deduct(Payroll payroll) {
        double deductionAmount = payroll.getGrossPay() * this.percentRate / 100.0;
        payroll.setNetPay(payroll.getNetPay() - deductionAmount);
    }

    public Tax() {
    }
    
    public Tax(Double percentRate, String name, String description) {
        this.percentRate = percentRate;
        this.taxName = name;
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getPercentRate() {
        return percentRate;
    }
    
    public void setPercentRate(Double percentRate) {
        this.percentRate = percentRate;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
