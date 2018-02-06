/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { DomainMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix-detail.component';
import { DomainMySuffixService } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix.service';
import { DomainMySuffix } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix.model';

describe('Component Tests', () => {

    describe('DomainMySuffix Management Detail Component', () => {
        let comp: DomainMySuffixDetailComponent;
        let fixture: ComponentFixture<DomainMySuffixDetailComponent>;
        let service: DomainMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [DomainMySuffixDetailComponent],
                providers: [
                    DomainMySuffixService
                ]
            })
            .overrideTemplate(DomainMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DomainMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DomainMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DomainMySuffix(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.domain).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
